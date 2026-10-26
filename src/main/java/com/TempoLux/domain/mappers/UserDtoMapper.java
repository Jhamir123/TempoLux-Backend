package com.TempoLux.domain.mappers;

import com.TempoLux.domain.dto.response.UserResponseDto;
import com.TempoLux.domain.model.User;

/**
 * Mapeo entre la entidad de dominio User y sus DTOs de salida.
 */
public final class UserDtoMapper {

    private UserDtoMapper() {
    }

    public static UserResponseDto toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMemberId(),
                user.getTier(),
                user.getPreferredCollection(),
                user.getJoinedDate());
    }
}
