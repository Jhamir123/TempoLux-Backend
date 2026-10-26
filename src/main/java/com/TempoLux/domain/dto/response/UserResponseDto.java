package com.TempoLux.domain.dto.response;

/**
 * Respuesta de registro / consulta de usuario, alineada con la estructura
 * que el frontend guarda en el perfil del socio (id, name, email, memberId,
 * tier, preferredCollection, joinedDate).
 */
public record UserResponseDto(
        Long id,
        String name,
        String email,
        String memberId,
        String tier,
        String preferredCollection,
        String joinedDate) {
}
