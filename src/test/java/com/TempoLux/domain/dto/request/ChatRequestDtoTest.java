package com.TempoLux.domain.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatRequestDtoTest {

    @Test
    void shouldCreateChatRequestDtoWithAllFields() {
        ChatRequestDto dto = new ChatRequestDto("chat-123", "¿Cuánto cuesta el Rolex Submariner?");

        assertThat(dto.chatId()).isEqualTo("chat-123");
        assertThat(dto.message()).isEqualTo("¿Cuánto cuesta el Rolex Submariner?");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        ChatRequestDto dto1 = new ChatRequestDto("chat-123", "Mensaje de prueba");
        ChatRequestDto dto2 = new ChatRequestDto("chat-123", "Mensaje de prueba");
        ChatRequestDto dto3 = new ChatRequestDto("chat-456", "Otro mensaje");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        ChatRequestDto dto = new ChatRequestDto("chat-123", "Hola");
        String toString = dto.toString();

        assertThat(toString).contains("chatId=chat-123");
        assertThat(toString).contains("message=Hola");
    }
}