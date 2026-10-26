package com.TempoLux.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Pedido registrado/confirmado. El orderId expuesto es el código único de orden
 * (ej. "ORD-2026-99A"), y status contiene la etiqueta en español del estado.
 */
public record OrderResponseDto(
        String orderId,
        Long userId,
        String customerName,
        String customerEmail,
        String status,
        BigDecimal total,
        List<OrderItemResponseDto> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
