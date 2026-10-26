package com.TempoLux.crosscutting.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicateResourceExceptionTest {

    @Test
    void shouldCreateDuplicateResourceExceptionWithMessage() {
        DuplicateResourceException exception = new DuplicateResourceException("Recurso duplicado");

        assertThat(exception.getMessage()).isEqualTo("Recurso duplicado");
    }

    @Test
    void shouldExtendRuntimeException() {
        DuplicateResourceException exception = new DuplicateResourceException("Error");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}