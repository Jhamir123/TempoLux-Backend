package com.TempoLux.domain.port;

import com.TempoLux.domain.model.Order;
import com.TempoLux.domain.model.OrderItem;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para persistenciar y consultar pedidos y sus líneas.
 */
public interface OrderRepository {

    Mono<Order> save(Order order);

    Mono<Order> findByOrderCode(String orderCode);

    Mono<Order> findById(Long id);

    Flux<OrderItem> saveOrderItems(List<OrderItem> items, Long orderId);

    Flux<OrderItem> findItemsByOrderId(Long orderId);
}
