package com.TempoLux.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Línea de un pedido: identifica el producto y la cantidad.
 * El precio unitario enviado por el cliente es orientativo: el backend siempre
 * recalcula los importes contra el catálogo para evitar manipulación.
 */
public record OrderItemRequestDto(
        @NotNull(message = "El product_id es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer quantity,

        BigDecimal unitPrice) {
}
