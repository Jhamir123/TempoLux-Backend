package com.TempoLux.domain.model.groq;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Definición enriquecida de una herramienta (función) que se envía a la API de Groq
 * en el campo "tools" del payload del chat. El esquema JSON de parámetros usa el
 * formato JSON Schema estándar de OpenAI/Groq.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroqToolDefinition {

    private String type;
    private GroqFunctionDefinition function;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GroqFunctionDefinition {

        private String name;
        private String description;
        private Map<String, Object> parameters;
    }
}
