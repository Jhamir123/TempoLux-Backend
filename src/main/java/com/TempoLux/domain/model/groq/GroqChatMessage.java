package com.TempoLux.domain.model.groq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mensaje de la conversación enviado a la API de Groq (role: system, user, assistant o tool).
 * Cuando el asistente solicita una herramienta, la propiedad toolCalls se puebla con las
 * llamadas y el contenido de texto queda vacío.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroqChatMessage {

    private String role;

    private String content;

    private String name;

    @JsonProperty("tool_call_id")
    private String toolCallId;

    @JsonProperty("tool_calls")
    private List<GroqToolCall> toolCalls;
}
