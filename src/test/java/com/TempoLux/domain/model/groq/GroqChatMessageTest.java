package com.TempoLux.domain.model.groq;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroqChatMessageTest {

    @Test
    void shouldCreateGroqChatMessageWithBuilder() {
        GroqChatMessage message = GroqChatMessage.builder()
                .role("user")
                .content("Hola")
                .build();

        assertThat(message.getRole()).isEqualTo("user");
        assertThat(message.getContent()).isEqualTo("Hola");
        assertThat(message.getToolCallId()).isNull();
    }

    @Test
    void shouldCreateGroqChatMessageWithAllFields() {
        GroqChatMessage message = GroqChatMessage.builder()
                .role("assistant")
                .content("Respuesta")
                .toolCallId("call_123")
                .build();

        assertThat(message.getRole()).isEqualTo("assistant");
        assertThat(message.getContent()).isEqualTo("Respuesta");
        assertThat(message.getToolCallId()).isEqualTo("call_123");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        GroqChatMessage msg1 = GroqChatMessage.builder().role("user").content("Test").build();
        GroqChatMessage msg2 = GroqChatMessage.builder().role("user").content("Test").build();
        GroqChatMessage msg3 = GroqChatMessage.builder().role("assistant").content("Test").build();

        assertThat(msg1).isEqualTo(msg2);
        assertThat(msg1.hashCode()).isEqualTo(msg2.hashCode());
        assertThat(msg1).isNotEqualTo(msg3);
    }
}