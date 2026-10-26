package com.TempoLux.domain.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserLoginRequestDtoTest {

    @Test
    void shouldCreateUserLoginRequestDtoWithAllFields() {
        UserLoginRequestDto dto = new UserLoginRequestDto(
                "juan@test.com",
                "password123"
        );

        assertThat(dto.email()).isEqualTo("juan@test.com");
        assertThat(dto.password()).isEqualTo("password123");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        UserLoginRequestDto dto1 = new UserLoginRequestDto("juan@test.com", "password123");
        UserLoginRequestDto dto2 = new UserLoginRequestDto("juan@test.com", "password123");
        UserLoginRequestDto dto3 = new UserLoginRequestDto("maria@test.com", "password456");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        UserLoginRequestDto dto = new UserLoginRequestDto("test@test.com", "password");
        String toString = dto.toString();

        assertThat(toString).contains("email=test@test.com");
        assertThat(toString).contains("password=password");
    }
}