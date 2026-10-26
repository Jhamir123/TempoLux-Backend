package com.TempoLux.crosscutting.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidCredentialsExceptionTest {

    @Test
    void shouldCreateInvalidCredentialsExceptionWithMessage() {
        InvalidCredentialsException exception = new InvalidCredentialsException("Credenciales inválidas");

        assertThat(exception.getMessage()).isEqualTo("Credenciales inválidas");
    }

    @Test
    void shouldExtendRuntimeException() {
        InvalidCredentialsException exception = new InvalidCredentialsException("Error");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}