package com.TempoLux.domain.dto.response;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseDtoTest {

    @Test
    void shouldCreateErrorResponseDtoWithAllFields() {
        Instant timestamp = Instant.parse("2026-01-15T10:30:00Z");
        ErrorResponseDto dto = new ErrorResponseDto(
                timestamp,
                404,
                "Not Found",
                "Producto no encontrado con id: 999",
                "/api/v1/products/999",
                Map.of()
        );

        assertThat(dto.timestamp()).isEqualTo(timestamp);
        assertThat(dto.status()).isEqualTo(404);
        assertThat(dto.error()).isEqualTo("Not Found");
        assertThat(dto.message()).isEqualTo("Producto no encontrado con id: 999");
        assertThat(dto.path()).isEqualTo("/api/v1/products/999");
        assertThat(dto.fieldErrors()).isEmpty();
    }

    @Test
    void shouldCreateErrorResponseDtoWithFieldErrors() {
        Instant timestamp = Instant.parse("2026-01-15T10:30:00Z");
        ErrorResponseDto dto = new ErrorResponseDto(
                timestamp,
                400,
                "Bad Request",
                "Solicitud inválida",
                "/api/v1/auth/register",
                Map.of("email", "El correo electrónico no tiene un formato válido")
        );

        assertThat(dto.fieldErrors()).hasSize(1);
        assertThat(dto.fieldErrors().get("email")).isEqualTo("El correo electrónico no tiene un formato válido");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        Instant timestamp = Instant.parse("2026-01-15T10:30:00Z");
        ErrorResponseDto dto1 = new ErrorResponseDto(timestamp, 404, "Not Found", "Msg", "/path", Map.of());
        ErrorResponseDto dto2 = new ErrorResponseDto(timestamp, 404, "Not Found", "Msg", "/path", Map.of());
        ErrorResponseDto dto3 = new ErrorResponseDto(timestamp, 500, "Error", "Msg", "/path", Map.of());

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        ErrorResponseDto dto = new ErrorResponseDto(Instant.now(), 404, "Not Found", "Msg", "/path", Map.of());
        String toString = dto.toString();

        assertThat(toString).contains("status=404");
        assertThat(toString).contains("error=Not Found");
        assertThat(toString).contains("message=Msg");
    }
}