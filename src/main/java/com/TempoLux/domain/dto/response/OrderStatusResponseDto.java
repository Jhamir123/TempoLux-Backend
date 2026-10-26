package com.TempoLux.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Estado de despacho de un pedido según su código único (order_id).
 * Es el DTO que LuxBot retorna a través de la herramienta get_order_status.
 */
public record OrderStatusResponseDto(
        String orderId,
        String status,
        Integer itemCount,
        BigDecimal total,
        List<OrderItemResponseDto> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
