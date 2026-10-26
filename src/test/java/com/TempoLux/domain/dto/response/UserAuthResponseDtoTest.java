package com.TempoLux.domain.dto.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserAuthResponseDtoTest {

    @Test
    void shouldCreateUserAuthResponseDtoWithAllFields() {
        UserResponseDto userDto = new UserResponseDto(1L, "Juan", "juan@test.com", "TL-ABC", "Gold", "Colección", "Octubre 2024");
        UserAuthResponseDto dto = new UserAuthResponseDto(
                "eyJhbGciOiJIUzI1NiJ9...",
                "Bearer",
                86400L,
                userDto
        );

        assertThat(dto.token()).isEqualTo("eyJhbGciOiJIUzI1NiJ9...");
        assertThat(dto.tokenType()).isEqualTo("Bearer");
        assertThat(dto.expiresIn()).isEqualTo(86400L);
        assertThat(dto.user()).isEqualTo(userDto);
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        UserResponseDto userDto = new UserResponseDto(1L, "Juan", "juan@test.com", "TL-ABC", "Gold", "Colección", "Octubre 2024");
        UserAuthResponseDto dto1 = new UserAuthResponseDto("token123", "Bearer", 86400L, userDto);
        UserAuthResponseDto dto2 = new UserAuthResponseDto("token123", "Bearer", 86400L, userDto);
        UserAuthResponseDto dto3 = new UserAuthResponseDto("token456", "Bearer", 86400L, userDto);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        UserResponseDto userDto = new UserResponseDto(1L, "Test", "test@test.com", "TL-TEST", "Tier", "Collection", "Date");
        UserAuthResponseDto dto = new UserAuthResponseDto("token", "Bearer", 86400L, userDto);
        String toString = dto.toString();

        assertThat(toString).contains("token=token");
        assertThat(toString).contains("tokenType=Bearer");
        assertThat(toString).contains("expiresIn=86400");
    }
}