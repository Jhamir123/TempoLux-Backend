package com.TempoLux.domain.model;

import com.TempoLux.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de dominio de un pedido de compra.
 * El código único de la orden (orderCode, ej. "ORD-2026-99A") es el identificador
 * público que se expone como order_id en la API para consultar el estado.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;
    private String orderCode;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
