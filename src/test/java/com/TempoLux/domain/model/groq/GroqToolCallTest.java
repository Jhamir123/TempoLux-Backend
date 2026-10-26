package com.TempoLux.domain.model.groq;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroqToolCallTest {

    @Test
    void shouldCreateGroqToolCallWithBuilder() {
        GroqFunctionCall function = GroqFunctionCall.builder()
                .name("search_products")
                .arguments("{\"query\": \"rolex\"}")
                .build();

        GroqToolCall toolCall = GroqToolCall.builder()
                .id("call_123")
                .type("function")
                .function(function)
                .build();

        assertThat(toolCall.getId()).isEqualTo("call_123");
        assertThat(toolCall.getType()).isEqualTo("function");
        assertThat(toolCall.getFunction()).isEqualTo(function);
        assertThat(toolCall.getFunction().getName()).isEqualTo("search_products");
        assertThat(toolCall.getFunction().getArguments()).isEqualTo("{\"query\": \"rolex\"}");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        GroqFunctionCall function = GroqFunctionCall.builder().name("test").arguments("{}").build();
        GroqToolCall tc1 = GroqToolCall.builder().id("call_1").type("function").function(function).build();
        GroqToolCall tc2 = GroqToolCall.builder().id("call_1").type("function").function(function).build();
        GroqToolCall tc3 = GroqToolCall.builder().id("call_2").type("function").function(function).build();

        assertThat(tc1).isEqualTo(tc2);
        assertThat(tc1.hashCode()).isEqualTo(tc2.hashCode());
        assertThat(tc1).isNotEqualTo(tc3);
    }
}