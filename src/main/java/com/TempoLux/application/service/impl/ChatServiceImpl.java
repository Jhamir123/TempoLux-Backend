package com.TempoLux.application.service.impl;

import com.TempoLux.crosscutting.config.GroqProperties;
import com.TempoLux.crosscutting.exception.ResourceNotFoundException;
import com.TempoLux.domain.constans.ApplicationConstants;
import com.TempoLux.domain.constans.LuxBotPrompts;
import com.TempoLux.domain.dto.request.ChatRequestDto;
import com.TempoLux.domain.dto.response.ChatResponseDto;
import com.TempoLux.domain.model.groq.GroqChatMessage;
import com.TempoLux.domain.model.groq.GroqChatRequest;
import com.TempoLux.domain.model.groq.GroqChatResponse;
import com.TempoLux.domain.model.groq.GroqFunctionCall;
import com.TempoLux.domain.model.groq.GroqToolCall;
import com.TempoLux.domain.model.groq.GroqToolDefinition;
import com.TempoLux.domain.port.GroqApiClient;
import com.TempoLux.application.service.ChatService;
import com.TempoLux.application.service.OrderService;
import com.TempoLux.application.service.ProductService;
import java.math.BigDecimal;
import java.time.Instant;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

/**
 * Caso de uso del asistente virtual LuxBot (LuxBot) con Tool Calling de Groq.
 *
 * Flujo reactivo:
 * Navegador -&gt; POST /api/v1/chat -&gt; WebClient a Groq -&gt; si Groq pide un tool_call
 * -&gt; backend consulta PostgreSQL con R2DBC -&gt; envía el resultado del tool a Groq
 * -&gt; Groq genera la respuesta final -&gt; se retorna al frontend.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final String ROLE_SYSTEM = "system";
    private static final String ROLE_USER = "user";
    private static final String ROLE_ASSISTANT = "assistant";
    private static final String ROLE_TOOL = "tool";
    private static final String ERROR_FIELD = "error";
    private static final String REQUIRED_FIELD = "required";
    private static final String FUNCTION_TYPE = "function";
    private static final String PRODUCT_ID_FIELD = "product_id";
    private static final String ORDER_ID_FIELD = "order_id";
    private static final String PROPERTIES_FIELD = "properties";
    private static final String OBJECT_TYPE = "object";

    private static final Set<String> SYSTEM_DATA_KEYWORDS = Set.of(
            "precio", "cuanto", "cuesta", "valor", "stock", "disponib", "hay", "tienen",
            "catalogo", "modelo", "reloj", "producto", "pedido", "orden", "especific",
            "ficha", "caracterist", "material", "caja", "correa", "resistencia", "movimiento",
            "calibre", "esfera", "bisel", "cristal", "garantia", "envio", "descuento", "promocion",
            "pendiente", "preparacion", "transito", "entregado", "despachado");
    private static final Pattern ORDER_CODE_PATTERN = Pattern.compile("(?i)ORD-\\d{4}-[0-9A-Z]+");

    private static final int MAX_GROUNDING_RETRIES = 1;

    private final GroqApiClient groqApiClient;
    private final GroqProperties groqProperties;
    private final ObjectMapper objectMapper;
    private final ProductService productService;
    private final OrderService orderService;

    /**
     * Sesiones activas de chat: chatId -&gt; historial (solo interacciones usuario/asistente).
     */
    private final Map<String, ChatSession> sessions = new ConcurrentHashMap<>();

    @Override
    public Mono<ChatResponseDto> chat(ChatRequestDto request) {
        String chatId = request.chatId();

        ChatSession session = sessions.computeIfAbsent(chatId, key -> new ChatSession());
        List<GroqChatMessage> workingMessages = session.addUserAndSnapshot(request.message());

        return runChat(workingMessages, 0, request.message(), false, 0)
                .flatMap(reply -> {
                    session.addAssistantReply(reply);
                    return Mono.just(new ChatResponseDto(chatId, reply, Instant.now().toString()));
                })
                .onErrorResume(error -> {
                    log.error("Error en la conversación [{}]: {}", chatId, error.getMessage());
                    String fallback = "Lo siento, en este momento no puedo conectarme con mi servicio de IA. "
                            + "Por favor, intenta nuevamente en unos instantes.";
                    session.addAssistantReply(fallback);
                    return Mono.just(new ChatResponseDto(chatId, fallback, Instant.now().toString()));
                });
    }

    /**
     * Ciclo de ejecución: pide a Groq, y si solicita tool_calls los ejecuta y vuelve a
     * llamar a Groq con los resultados hasta obtener una respuesta final en texto.
     *
     * Control de fundamentación: si la consulta del cliente pide datos del sistema y el modelo
     * devuelve texto sin haber ejecutado ninguna herramienta, la respuesta se descarta, se
     * reintenta con el recordatorio de fundamentación y, si vuelve a fallar, se devuelve un
     * mensaje controlado. Así el cliente nunca recibe datos inventados.
     */
    private Mono<String> runChat(List<GroqChatMessage> workingMessages, int iteration, String userText,
                                 boolean toolsUsed, int groundingRetries) {
        List<GroqChatMessage> conversation = new ArrayList<>();
        String systemPrompt = !toolsUsed && groundingRetries > 0
                ? LuxBotPrompts.SYSTEM_PROMPT + "\n\n" + LuxBotPrompts.GROUNDING_REMINDER
                : LuxBotPrompts.SYSTEM_PROMPT;
        conversation.add(GroqChatMessage.builder().role(ROLE_SYSTEM).content(systemPrompt).build());
        conversation.addAll(workingMessages);

        GroqChatRequest request = GroqChatRequest.builder()
                .model(groqProperties.getModel())
                .messages(conversation)
                .tools(buildToolDefinitions())
                .toolChoice("auto")
                .temperature(ApplicationConstants.CHAT_TEMPERATURE)
                .maxTokens(ApplicationConstants.CHAT_MAX_TOKENS)
                .build();

        return groqApiClient.chat(request)
                .flatMap(response -> handleResponse(
                        response, workingMessages, iteration, userText, toolsUsed, groundingRetries));
    }

    private Mono<String> handleResponse(GroqChatResponse response, List<GroqChatMessage> workingMessages,
                                        int iteration, String userText, boolean toolsUsed, int groundingRetries) {
        if (response.getChoices() == null || response.getChoices().isEmpty()) {
            return Mono.just("No pude generar una respuesta en este momento.");
        }

        GroqChatMessage assistant = response.getChoices().get(0).getMessage();
        List<GroqToolCall> toolCalls = assistant.getToolCalls();
        if (toolCalls != null && !toolCalls.isEmpty()) {
            return handleToolCalls(workingMessages, assistant, toolCalls, iteration, userText, groundingRetries);
        }

        return handleAssistantReply(assistant.getContent(), workingMessages, iteration, userText, toolsUsed,
                groundingRetries);
    }

    private Mono<String> handleToolCalls(List<GroqChatMessage> workingMessages, GroqChatMessage assistant,
                                         List<GroqToolCall> toolCalls, int iteration, String userText,
                                         int groundingRetries) {
        if (iteration >= ApplicationConstants.CHAT_MAX_TOOL_ITERATIONS) {
            return Mono.just("He alcanzado el límite de consultas para esta pregunta. "
                    + "Por favor, inténtalo de nuevo.");
        }

        return executeToolCalls(workingMessages, assistant, toolCalls)
                .flatMap(updated -> runChat(updated, iteration + 1, userText, true, groundingRetries));
    }

    private Mono<String> handleAssistantReply(String content, List<GroqChatMessage> workingMessages,
                                              int iteration, String userText, boolean toolsUsed,
                                              int groundingRetries) {
        if (content == null || content.isBlank()) {
            return Mono.just("Entendido. ¿Hay algo más en lo que pueda ayudarte con el catálogo de TempoLux?");
        }
        if (toolsUsed || !requiresSystemData(userText)) {
            return Mono.just(content);
        }
        if (groundingRetries >= MAX_GROUNDING_RETRIES) {
            log.warn("LuxBot insiste en afirmar datos sin respaldo del sistema. Se devuelve mensaje controlado.");
            return Mono.just(LuxBotPrompts.UNVERIFIED_ANSWER_MESSAGE);
        }

        log.warn("LuxBot afirmó datos del sistema sin consultar una herramienta. Se reintenta.");
        return runChat(workingMessages, iteration, userText, false, groundingRetries + 1);
    }

    /**
     * true si la consulta del cliente requiere datos reales del catálogo o de un pedido.
     */
    private boolean requiresSystemData(String userText) {
        if (userText == null) {
            return false;
        }
        String normalizedText = normalizeText(userText);
        return ORDER_CODE_PATTERN.matcher(normalizedText).find()
                || SYSTEM_DATA_KEYWORDS.stream().anyMatch(normalizedText::contains);
    }

    private String normalizeText(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    /**
     * Ejecuta en orden las herramientas solicitadas por el modelo y construye la lista
     * de mensajes (assistant con tool_calls + respuestas tool) para la siguiente iteración.
     */
    private Mono<List<GroqChatMessage>> executeToolCalls(List<GroqChatMessage> workingMessages,
                                                         GroqChatMessage assistant,
                                                         List<GroqToolCall> toolCalls) {
        return Flux.fromIterable(toolCalls)
                .concatMap(this::executeTool)
                .collectList()
                .map(results -> {
                    List<GroqChatMessage> updated = new ArrayList<>(workingMessages);
                    updated.add(assistant);
                    for (int i = 0; i < toolCalls.size(); i++) {
                        updated.add(GroqChatMessage.builder()
                                .role(ROLE_TOOL)
                                .content(results.get(i))
                                .toolCallId(toolCalls.get(i).getId())
                                .build());
                    }
                    return updated;
                });
    }

    /**
     * Despacha cada tool_call a la función correspondiente de TempoLux.
     */
    private Mono<String> executeTool(GroqToolCall toolCall) {
        if (toolCall == null || toolCall.getFunction() == null) {
            return Mono.just(toJson(Map.of(ERROR_FIELD, "No se pudo identificar la función solicitada.")));
        }
        GroqFunctionCall function = toolCall.getFunction();
        String name = function.getName();
        if (name == null || name.isBlank()) {
            return Mono.just(toJson(Map.of(ERROR_FIELD, "La funcion solicitada no tiene nombre.")));
        }
        Map<String, Object> args = parseArguments(function.getArguments());

        log.debug("LuxBot ejecuta tool_call -> [{}] args={}", name, args);

        return switch (name) {
            case "search_products" -> executeSearchProducts(args);
            case "get_product_details" -> executeGetProductDetails(args);
            case "get_order_status" -> executeGetOrderStatus(args);
            default -> Mono.just(toJson(Map.of(ERROR_FIELD, "Función desconocida: " + name)));
        };
    }

    private Mono<String> executeSearchProducts(Map<String, Object> args) {
        String query = strArg(args, "query");
        String category = strArg(args, "category");
        BigDecimal maxPrice = numberArg(args, "max_price");

        return productService.searchProducts(query, category, maxPrice)
                .collectList()
                .map(this::toJson);
    }

    private Mono<String> executeGetProductDetails(Map<String, Object> args) {
        Long productId = longArg(args, PRODUCT_ID_FIELD);
        if (productId == null || productId <= 0) {
            return Mono.just(toJson(Map.of(ERROR_FIELD, "product_id debe ser un numero positivo.")));
        }
        return productService.getProductDetails(productId)
                .map(this::toJson)
                .onErrorResume(ResourceNotFoundException.class,
                        error -> Mono.just(toJson(Map.of(ERROR_FIELD, error.getMessage()))));
    }

    private Mono<String> executeGetOrderStatus(Map<String, Object> args) {
        String orderId = strArg(args, ORDER_ID_FIELD);
        if (orderId == null) {
            return Mono.just(toJson(Map.of(ERROR_FIELD, "order_id es obligatorio.")));
        }
        return orderService.getOrderStatus(orderId)
                .map(this::toJson)
                .onErrorResume(ResourceNotFoundException.class,
                        error -> Mono.just(toJson(Map.of(ERROR_FIELD, error.getMessage()))));
    }

    private Map<String, Object> parseArguments(String arguments) {
        if (arguments == null || arguments.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(arguments, new TypeReference<Map<String, Object>>() {
            });
        } catch (JacksonException e) {
            log.warn("No se pudieron parsear los argumentos del tool_call: {}", arguments);
            return Map.of();
        }
    }

    private String strArg(Map<String, Object> args, String key) {
        Object value = args.get(key);
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        return text.isEmpty() ? null : text;
    }

    private Long longArg(Map<String, Object> args, String key) {
        Object value = args.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal numberArg(Map<String, Object> args, String key) {
        Object value = args.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        try {
            return new BigDecimal(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JacksonException e) {
            log.warn("No se pudo serializar el resultado del tool_call", e);
            return "{}";
        }
    }

    private final class ChatSession {

        private final List<GroqChatMessage> history = new ArrayList<>();

        private synchronized List<GroqChatMessage> addUserAndSnapshot(String message) {
            history.add(GroqChatMessage.builder().role(ROLE_USER).content(message).build());
            trimHistory();
            return new ArrayList<>(history);
        }

        private synchronized void addAssistantReply(String reply) {
            history.add(GroqChatMessage.builder().role(ROLE_ASSISTANT).content(reply).build());
            trimHistory();
        }

        private void trimHistory() {
            if (history.size() > ApplicationConstants.CHAT_MAX_HISTORY_MESSAGES) {
                List<GroqChatMessage> tail = new ArrayList<>(
                        history.subList(history.size() - ApplicationConstants.CHAT_MAX_HISTORY_MESSAGES,
                                history.size()));
                history.clear();
                history.addAll(tail);
            }
        }
    }

    /**
     * Definición exacta de las 3 herramientas expuestas a Groq.
     */
    private List<GroqToolDefinition> buildToolDefinitions() {
        Map<String, Object> searchParams = new LinkedHashMap<>();
        searchParams.put("type", OBJECT_TYPE);
        searchParams.put(PROPERTIES_FIELD, Map.of(
                "query", jsonStringProp("Término de búsqueda en nombre, descripción, serie o SKU (ej. \"cronógrafo\", \"suizo\")"),
                "category", jsonStringProp("Categoría exacta del catálogo: \"Alta Relojería\", \"Deportivo\" o \"Artesanal 3D\""),
                "max_price", jsonNumberProp("Precio máximo en USD (filtro por presupuesto del cliente)")));
        searchParams.put(REQUIRED_FIELD, List.of());

        Map<String, Object> detailsParams = new LinkedHashMap<>();
        detailsParams.put("type", OBJECT_TYPE);
        detailsParams.put(PROPERTIES_FIELD, Map.of(
                PRODUCT_ID_FIELD, jsonNumberProp("ID numérico del reloj (campo \"id\" del catálogo)")));
        detailsParams.put(REQUIRED_FIELD, List.of(PRODUCT_ID_FIELD));

        Map<String, Object> orderParams = new LinkedHashMap<>();
        orderParams.put("type", OBJECT_TYPE);
        orderParams.put(PROPERTIES_FIELD, Map.of(
                ORDER_ID_FIELD, jsonStringProp("Código único del pedido (formato ORD-AAAA-XXXX, ej. ORD-2026-99A)")));
        orderParams.put(REQUIRED_FIELD, List.of(ORDER_ID_FIELD));

        return List.of(
                GroqToolDefinition.builder()
                        .type(FUNCTION_TYPE)
                        .function(GroqToolDefinition.GroqFunctionDefinition.builder()
                                .name("search_products")
                                .description("Busca relojes en el catálogo de TempoLux por término (query), categoría y/o precio máximo. " +
                                        "Devuelve la lista de productos con nombre, precio y disponibilidad.")
                                .parameters(searchParams)
                                .build())
                        .build(),
                GroqToolDefinition.builder()
                        .type(FUNCTION_TYPE)
                        .function(GroqToolDefinition.GroqFunctionDefinition.builder()
                                .name("get_product_details")
                                .description("Obtiene las especificaciones técnicas completas de un reloj (material de caja, correa, " +
                                        "resistencia al agua, cristal, movimiento, modelo 3D) y su stock actual.")
                                .parameters(detailsParams)
                                .build())
                        .build(),
                GroqToolDefinition.builder()
                        .type(FUNCTION_TYPE)
                        .function(GroqToolDefinition.GroqFunctionDefinition.builder()
                                .name("get_order_status")
                                .description("Consulta el estado de despacho de un pedido por su código único de orden " +
                                        "(Pendiente, En Preparación, En Tránsito, Entregado).")
                                .parameters(orderParams)
                                .build())
                        .build());
    }

    private Map<String, Object> jsonStringProp(String description) {
        return Map.of("type", "string", "description", description);
    }

    private Map<String, Object> jsonNumberProp(String description) {
        return Map.of("type", "number", "description", description);
    }
}
