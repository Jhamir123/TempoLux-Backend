package com.TempoLux.infrastructure.groq;

import com.TempoLux.crosscutting.exception.ChatServiceException;
import com.TempoLux.domain.constans.ApplicationConstants;
import com.TempoLux.domain.model.groq.GroqChatRequest;
import com.TempoLux.domain.model.groq.GroqChatResponse;
import com.TempoLux.domain.port.GroqApiClient;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Adaptador hacia la API de Groq mediante WebClient reactivo.
 * Transforma los errores HTTP en ChatServiceException con un mensaje legible.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroqApiClientImpl implements GroqApiClient {

    @Qualifier(ApplicationConstants.GROQ_WEB_CLIENT_BEAN)
    private final WebClient webClient;

    @Override
    public Mono<GroqChatResponse> chat(GroqChatRequest request) {
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> new ChatServiceException(
                                        "Error de Groq API (" + response.statusCode().value() + "): " + body)))
                .bodyToMono(GroqChatResponse.class)
                .doOnError(error -> log.error("Fallo al comunicarse con Groq API", error));
    }
}
