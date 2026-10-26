package com.TempoLux.crosscutting.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateResourceNotFoundExceptionWithMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Recurso no encontrado");

        assertThat(exception.getMessage()).isEqualTo("Recurso no encontrado");
    }

    @Test
    void shouldExtendRuntimeException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Error");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}