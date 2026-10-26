package com.TempoLux.crosscutting.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    @Test
    void shouldCreateBusinessExceptionWithMessage() {
        BusinessException exception = new BusinessException("Error de negocio");

        assertThat(exception.getMessage()).isEqualTo("Error de negocio");
    }

    @Test
    void shouldExtendRuntimeException() {
        BusinessException exception = new BusinessException("Error");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}