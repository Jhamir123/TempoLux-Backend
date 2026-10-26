package com.TempoLux.domain.mappers;

import com.TempoLux.domain.entity.OrderEntity;
import com.TempoLux.domain.enums.OrderStatus;
import com.TempoLux.domain.model.Order;
import com.TempoLux.domain.model.OrderItem;
import org.springframework.stereotype.Component;

/**
 * Mapeo entre las entidades de dominio Order/OrderItem (model) y los modelos de
 * persistencia R2DBC Order/OrderItem (entity).
 */
@Component
public class OrderPersistenceMapper {

    public Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        return Order.builder()
                .id(entity.getId())
                .orderCode(entity.getOrderCode())
                .userId(entity.getUserId())
                .customerName(entity.getCustomerName())
                .customerEmail(entity.getCustomerEmail())
                .status(OrderStatus.fromValue(entity.getStatus()))
                .total(entity.getTotal())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public OrderEntity toPersistence(Order order) {
        if (order == null) {
            return null;
        }
        return OrderEntity.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .userId(order.getUserId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .status(order.getStatus().name())
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public OrderItem toDomain(com.TempoLux.domain.entity.OrderItemEntity entity) {
        if (entity == null) {
            return null;
        }
        return OrderItem.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .subtotal(entity.getSubtotal())
                .build();
    }

    public com.TempoLux.domain.entity.OrderItemEntity toPersistence(OrderItem item, Long orderId) {
        if (item == null) {
            return null;
        }
        return com.TempoLux.domain.entity.OrderItemEntity.builder()
                .id(item.getId())
                .orderId(orderId)
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
