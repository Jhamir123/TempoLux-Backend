package com.TempoLux.domain.dto.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseDtoTest {

    @Test
    void shouldCreateUserResponseDtoWithAllFields() {
        UserResponseDto dto = new UserResponseDto(
                1L,
                "Juan Pérez",
                "juan@test.com",
                "TL-ABC123-CH",
                "Gold",
                "Alta Relojería",
                "Octubre 2024"
        );

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Juan Pérez");
        assertThat(dto.email()).isEqualTo("juan@test.com");
        assertThat(dto.memberId()).isEqualTo("TL-ABC123-CH");
        assertThat(dto.tier()).isEqualTo("Gold");
        assertThat(dto.preferredCollection()).isEqualTo("Alta Relojería");
        assertThat(dto.joinedDate()).isEqualTo("Octubre 2024");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        UserResponseDto dto1 = new UserResponseDto(1L, "Juan", "juan@test.com", "TL-ABC", "Gold", "Colección", "Octubre 2024");
        UserResponseDto dto2 = new UserResponseDto(1L, "Juan", "juan@test.com", "TL-ABC", "Gold", "Colección", "Octubre 2024");
        UserResponseDto dto3 = new UserResponseDto(2L, "Pedro", "pedro@test.com", "TL-XYZ", "Silver", "Otra", "Noviembre 2024");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        UserResponseDto dto = new UserResponseDto(1L, "Test", "test@test.com", "TL-TEST", "Tier", "Collection", "Date");
        String toString = dto.toString();

        assertThat(toString).contains("id=1");
        assertThat(toString).contains("name=Test");
        assertThat(toString).contains("email=test@test.com");
    }
}