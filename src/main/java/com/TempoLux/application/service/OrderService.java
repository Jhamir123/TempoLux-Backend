package com.TempoLux.application.service;

import com.TempoLux.domain.dto.request.OrderCreateRequestDto;
import com.TempoLux.domain.dto.response.OrderResponseDto;
import com.TempoLux.domain.dto.response.OrderStatusResponseDto;
import reactor.core.publisher.Mono;

/**
 * Servicio de pedidos (registro, confirmación y seguimiento).
 * Contrato de la capa de aplicación; implementado en application.service.
 */
public interface OrderService {

    Mono<OrderResponseDto> createOrder(String customerEmail, OrderCreateRequestDto request);

    Mono<OrderStatusResponseDto> getOrderStatus(String orderId);
}
