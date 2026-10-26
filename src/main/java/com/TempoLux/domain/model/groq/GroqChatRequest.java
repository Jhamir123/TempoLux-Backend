package com.TempoLux.domain.model.groq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload de petición a Groq: endpoint POST /openai/v1/chat/completions.
 * Incluye mensajes conversacionales, herramientas disponibles y modo auto de elección.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroqChatRequest {

    private String model;

    private List<GroqChatMessage> messages;

    private List<GroqToolDefinition> tools;

    @JsonProperty("tool_choice")
    private String toolChoice;

    private Double temperature;

    @JsonProperty("max_tokens")
    private Integer maxTokens;
}
