package com.TempoLux.domain.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRegisterRequestDtoTest {

    @Test
    void shouldCreateUserRegisterRequestDtoWithAllFields() {
        UserRegisterRequestDto dto = new UserRegisterRequestDto(
                "Juan Pérez",
                "juan@test.com",
                "password123",
                "Alta Relojería"
        );

        assertThat(dto.name()).isEqualTo("Juan Pérez");
        assertThat(dto.email()).isEqualTo("juan@test.com");
        assertThat(dto.password()).isEqualTo("password123");
        assertThat(dto.preferredCollection()).isEqualTo("Alta Relojería");
    }

    @Test
    void shouldCreateUserRegisterRequestDtoWithoutPreferredCollection() {
        UserRegisterRequestDto dto = new UserRegisterRequestDto(
                "María García",
                "maria@test.com",
                "password456",
                null
        );

        assertThat(dto.name()).isEqualTo("María García");
        assertThat(dto.email()).isEqualTo("maria@test.com");
        assertThat(dto.password()).isEqualTo("password456");
        assertThat(dto.preferredCollection()).isNull();
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        UserRegisterRequestDto dto1 = new UserRegisterRequestDto("Juan", "juan@test.com", "pass123", "Colección");
        UserRegisterRequestDto dto2 = new UserRegisterRequestDto("Juan", "juan@test.com", "pass123", "Colección");
        UserRegisterRequestDto dto3 = new UserRegisterRequestDto("Pedro", "pedro@test.com", "pass456", "Otra");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        UserRegisterRequestDto dto = new UserRegisterRequestDto("Test", "test@test.com", "password", "Test Collection");
        String toString = dto.toString();

        assertThat(toString).contains("name=Test");
        assertThat(toString).contains("email=test@test.com");
        assertThat(toString).contains("password=password");
        assertThat(toString).contains("preferredCollection=Test Collection");
    }
}