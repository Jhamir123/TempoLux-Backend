package com.TempoLux.domain.mappers;

import com.TempoLux.domain.dto.response.OrderItemResponseDto;
import com.TempoLux.domain.dto.response.OrderResponseDto;
import com.TempoLux.domain.dto.response.OrderStatusResponseDto;
import com.TempoLux.domain.model.Order;
import com.TempoLux.domain.model.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapeo entre la entidad de dominio Order (+ OrderItem) y sus DTOs de salida.
 * Expone el estado en español (label) para que coincida con el frontend y LuxBot.
 */
public final class OrderDtoMapper {

    private OrderDtoMapper() {
    }

    public static OrderResponseDto toResponse(Order order, List<OrderItem> items) {
        return new OrderResponseDto(
                order.getOrderCode(),
                order.getUserId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getStatus().getLabel(),
                order.getTotal(),
                items == null ? List.of() : items.stream().map(OrderDtoMapper::toItemResponse).collect(Collectors.toList()),
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    public static OrderStatusResponseDto toStatusResponse(Order order, List<OrderItem> items) {
        int itemCount = items == null ? 0 : items.stream().mapToInt(OrderItem::getQuantity).sum();
        List<OrderItemResponseDto> detail = items == null
                ? List.of()
                : items.stream().map(OrderDtoMapper::toItemResponse).collect(Collectors.toList());
        return new OrderStatusResponseDto(
                order.getOrderCode(),
                order.getStatus().getLabel(),
                itemCount,
                order.getTotal(),
                detail,
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    public static OrderItemResponseDto toItemResponse(OrderItem item) {
        return new OrderItemResponseDto(
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal());
    }
}
