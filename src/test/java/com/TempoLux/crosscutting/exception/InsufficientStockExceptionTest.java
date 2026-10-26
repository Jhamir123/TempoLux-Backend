package com.TempoLux.crosscutting.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InsufficientStockExceptionTest {

    @Test
    void shouldCreateInsufficientStockExceptionWithProductId() {
        InsufficientStockException exception = new InsufficientStockException(123L);

        assertThat(exception.getMessage()).isEqualTo("Stock insuficiente para el producto con id: 123");
        assertThat(exception.getProductId()).isEqualTo(123L);
    }

    @Test
    void shouldExtendRuntimeException() {
        InsufficientStockException exception = new InsufficientStockException(456L);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}