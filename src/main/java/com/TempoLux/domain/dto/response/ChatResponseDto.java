package com.TempoLux.domain.dto.response;

/**
 * Respuesta del asistente virtual LuxBot (POST /api/v1/chat).
 */
public record ChatResponseDto(
        String chatId,
        String reply,
        String timestamp) {
}
