package com.TempoLux.domain.model.groq;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GroqChatRequestTest {

    @Test
    void shouldCreateGroqChatRequestWithBuilder() {
        GroqChatMessage message = GroqChatMessage.builder().role("user").content("Hola").build();
        GroqToolDefinition tool = GroqToolDefinition.builder()
                .type("function")
                .function(GroqToolDefinition.GroqFunctionDefinition.builder()
                        .name("search_products")
                        .description("Busca productos")
                        .parameters(Map.of())
                        .build())
                .build();

        GroqChatRequest request = GroqChatRequest.builder()
                .model("test-model")
                .messages(List.of(message))
                .tools(List.of(tool))
                .toolChoice("auto")
                .temperature(0.3)
                .maxTokens(1024)
                .build();

        assertThat(request.getModel()).isEqualTo("test-model");
        assertThat(request.getMessages()).hasSize(1);
        assertThat(request.getTools()).hasSize(1);
        assertThat(request.getToolChoice()).isEqualTo("auto");
        assertThat(request.getTemperature()).isEqualTo(0.3);
        assertThat(request.getMaxTokens()).isEqualTo(1024);
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        GroqChatRequest req1 = GroqChatRequest.builder().model("model").messages(List.of()).tools(List.of()).toolChoice("auto").temperature(0.3).maxTokens(1024).build();
        GroqChatRequest req2 = GroqChatRequest.builder().model("model").messages(List.of()).tools(List.of()).toolChoice("auto").temperature(0.3).maxTokens(1024).build();
        GroqChatRequest req3 = GroqChatRequest.builder().model("other").messages(List.of()).tools(List.of()).toolChoice("auto").temperature(0.3).maxTokens(1024).build();

        assertThat(req1).isEqualTo(req2);
        assertThat(req1.hashCode()).isEqualTo(req2.hashCode());
        assertThat(req1).isNotEqualTo(req3);
    }
}