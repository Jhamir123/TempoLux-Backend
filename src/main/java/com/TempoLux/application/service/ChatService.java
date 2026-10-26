package com.TempoLux.application.service;

import com.TempoLux.domain.dto.request.ChatRequestDto;
import com.TempoLux.domain.dto.response.ChatResponseDto;
import reactor.core.publisher.Mono;

/**
 * Servicio del asistente virtual LuxBot.
 * Contrato de la capa de aplicación; implementado en application.service.
 */
public interface ChatService {

    Mono<ChatResponseDto> chat(ChatRequestDto request);
}
