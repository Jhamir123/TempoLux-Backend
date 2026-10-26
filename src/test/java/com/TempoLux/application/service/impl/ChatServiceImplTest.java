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
import com.TempoLux.domain.port.GroqApiClient;
import com.TempoLux.application.service.ChatService;
import com.TempoLux.application.service.OrderService;
import com.TempoLux.application.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private GroqApiClient groqApiClient;

    @Mock
    private GroqProperties groqProperties;

    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    private ChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl(groqApiClient, groqProperties, new ObjectMapper(), productService, orderService);
    }

    @Test
    void shouldCreateNewChatSession() {
        when(groqProperties.getModel()).thenReturn("test-model");

        ChatRequestDto request = new ChatRequestDto("chat-123", "Hola");

        GroqChatResponse response = createGroqResponse("¡Hola! ¿En qué puedo ayudarte?");
        when(groqApiClient.chat(any(GroqChatRequest.class))).thenReturn(Mono.just(response));

        StepVerifier.create(chatService.chat(request))
                .expectNextMatches(dto -> {
                    assertThat(dto.chatId()).isEqualTo("chat-123");
                    assertThat(dto.reply()).contains("Hola");
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldHandleGroqErrorWithFallback() {
        when(groqProperties.getModel()).thenReturn("test-model");

        ChatRequestDto request = new ChatRequestDto("chat-789", "Hola");

        when(groqApiClient.chat(any(GroqChatRequest.class)))
                .thenReturn(Mono.error(new RuntimeException("Groq API error")));

        StepVerifier.create(chatService.chat(request))
                .expectNextMatches(dto -> {
                    assertThat(dto.chatId()).isEqualTo("chat-789");
                    assertThat(dto.reply()).contains("no puedo conectarme");
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldRejectCatalogAnswerWhenGroqDidNotUseATool() {
        when(groqProperties.getModel()).thenReturn("test-model");
        GroqChatResponse ungroundedResponse = createGroqResponse("El precio es 950 dÃ³lares.");
        when(groqApiClient.chat(any(GroqChatRequest.class)))
                .thenReturn(Mono.just(ungroundedResponse), Mono.just(ungroundedResponse));

        StepVerifier.create(chatService.chat(new ChatRequestDto("chat-grounding", "Â¿CuÃ¡l es el precio del reloj?")))
                .expectNextMatches(dto -> dto.reply().equals(LuxBotPrompts.UNVERIFIED_ANSWER_MESSAGE))
                .verifyComplete();

        verify(groqApiClient, times(2)).chat(any(GroqChatRequest.class));
    }

    private GroqChatResponse createGroqResponse(String content) {
        GroqChatResponse.Choice choice = new GroqChatResponse.Choice();
        GroqChatMessage message = GroqChatMessage.builder()
                .role("assistant")
                .content(content)
                .build();
        choice.setMessage(message);
        choice.setIndex(0);

        GroqChatResponse response = new GroqChatResponse();
        response.setChoices(List.of(choice));
        response.setModel("test-model");
        return response;
    }
}
