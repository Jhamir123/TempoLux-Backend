package com.TempoLux.domain.dto.response;

import java.math.BigDecimal;

/**
 * Línea de un pedido confirmado.
 */
public record OrderItemResponseDto(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal) {
}
