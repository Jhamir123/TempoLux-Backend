package com.TempoLux.domain.dto.response;

/**
 * Respuesta del login: token JWT reactivo + datos del usuario autenticado.
 */
public record UserAuthResponseDto(
        String token,
        String tokenType,
        long expiresIn,
        UserResponseDto user) {
}
