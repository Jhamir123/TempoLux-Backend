package com.TempoLux.domain.mappers;

import com.TempoLux.domain.dto.response.UserResponseDto;
import com.TempoLux.domain.model.User;
import com.TempoLux.domain.enums.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoMapperTest {

    @Test
    void shouldMapUserToResponseDto() {
        User user = User.builder()
                .id(1L)
                .name("Juan Pérez")
                .email("juan@test.com")
                .memberId("TL-ABC123-CH")
                .tier("Gold")
                .preferredCollection("Alta Relojería")
                .joinedDate("Octubre 2024")
                .role(UserRole.CLIENT)
                .createdAt(LocalDateTime.of(2024, 10, 1, 10, 0))
                .build();

        UserResponseDto dto = UserDtoMapper.toResponse(user);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Juan Pérez");
        assertThat(dto.email()).isEqualTo("juan@test.com");
        assertThat(dto.memberId()).isEqualTo("TL-ABC123-CH");
        assertThat(dto.tier()).isEqualTo("Gold");
        assertThat(dto.preferredCollection()).isEqualTo("Alta Relojería");
        assertThat(dto.joinedDate()).isEqualTo("Octubre 2024");
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        UserResponseDto dto = UserDtoMapper.toResponse(null);
        assertThat(dto).isNull();
    }

    @Test
    void shouldMapUserWithMinimalFields() {
        User user = User.builder()
                .id(2L)
                .name("María García")
                .email("maria@test.com")
                .build();

        UserResponseDto dto = UserDtoMapper.toResponse(user);

        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.name()).isEqualTo("María García");
        assertThat(dto.email()).isEqualTo("maria@test.com");
        assertThat(dto.memberId()).isNull();
        assertThat(dto.tier()).isNull();
        assertThat(dto.preferredCollection()).isNull();
        assertThat(dto.joinedDate()).isNull();
    }
}