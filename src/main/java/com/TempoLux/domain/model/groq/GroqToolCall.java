package com.TempoLux.domain.model.groq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tool call solicitada por el modelo de Groq durante el ciclo de ejecución.
 * Contiene el id de la llamada (obligatorio para el mensaje de tipo tool) y
 * la función invocada con sus argumentos serializados en JSON.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroqToolCall {

    private String id;
    private String type;
    private GroqFunctionCall function;
}
