package com.TempoLux.domain.port;

import com.TempoLux.domain.model.groq.GroqChatRequest;
import com.TempoLux.domain.model.groq.GroqChatResponse;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida hacia la API de Groq (inferencia de alta velocidad para LuxBot).
 */
public interface GroqApiClient {

    Mono<GroqChatResponse> chat(GroqChatRequest request);
}
