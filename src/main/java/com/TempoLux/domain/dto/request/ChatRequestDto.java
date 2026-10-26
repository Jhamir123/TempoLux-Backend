package com.TempoLux.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Cuerpo del endpoint POST /api/v1/chat.
 * El chatId/sessionId permite al backend mantener el historial de la conversación de
 * LuxBot. El historial se gestiona por sesión, no por mensaje.
 */
public record ChatRequestDto(
        @NotBlank(message = "El chatId es obligatorio")
        String chatId,

        @NotBlank(message = "El mensaje no puede estar vacío")
        String message) {
}
