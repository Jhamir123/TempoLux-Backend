package com.TempoLux.domain.model.groq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Función invocada por el modelo: nombre exacto y argumentos como string JSON
 * (ej. {"query":"cronógrafo","max_price":15000}).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroqFunctionCall {

    private String name;
    private String arguments;
}
