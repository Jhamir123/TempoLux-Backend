package com.TempoLux.crosscutting.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatServiceExceptionTest {

    @Test
    void shouldCreateChatServiceExceptionWithMessage() {
        ChatServiceException exception = new ChatServiceException("Error en servicio de chat");

        assertThat(exception.getMessage()).isEqualTo("Error en servicio de chat");
    }

    @Test
    void shouldExtendRuntimeException() {
        ChatServiceException exception = new ChatServiceException("Error");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}