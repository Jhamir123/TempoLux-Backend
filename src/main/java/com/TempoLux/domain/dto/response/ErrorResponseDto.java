package com.TempoLux.domain.dto.response;

import java.time.Instant;
import java.util.Map;

/**
 * Cuerpo uniforme de error JSON para toda la API reactiva de TempoLux.
 */
public record ErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors) {
}
