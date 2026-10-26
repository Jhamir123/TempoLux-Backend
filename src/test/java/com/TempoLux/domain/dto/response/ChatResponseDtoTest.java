package com.TempoLux.domain.dto.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatResponseDtoTest {

    @Test
    void shouldCreateChatResponseDtoWithAllFields() {
        ChatResponseDto dto = new ChatResponseDto("chat-123", "El Rolex Submariner cuesta USD 8,500.00", "2026-01-15T10:30:00Z");

        assertThat(dto.chatId()).isEqualTo("chat-123");
        assertThat(dto.reply()).isEqualTo("El Rolex Submariner cuesta USD 8,500.00");
        assertThat(dto.timestamp()).isEqualTo("2026-01-15T10:30:00Z");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        ChatResponseDto dto1 = new ChatResponseDto("chat-123", "Respuesta", "2026-01-15T10:30:00Z");
        ChatResponseDto dto2 = new ChatResponseDto("chat-123", "Respuesta", "2026-01-15T10:30:00Z");
        ChatResponseDto dto3 = new ChatResponseDto("chat-456", "Otra respuesta", "2026-01-15T10:30:00Z");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        ChatResponseDto dto = new ChatResponseDto("chat-123", "Hola, ¿en qué puedo ayudarte?", "2026-01-15T10:30:00Z");
        String toString = dto.toString();

        assertThat(toString).contains("chatId=chat-123");
        assertThat(toString).contains("reply=Hola, ¿en qué puedo ayudarte?");
    }
}