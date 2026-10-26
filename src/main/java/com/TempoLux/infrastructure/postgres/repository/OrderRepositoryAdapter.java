package com.TempoLux.infrastructure.postgres.repository;

import com.TempoLux.domain.mappers.OrderPersistenceMapper;
import com.TempoLux.domain.model.Order;
import com.TempoLux.domain.model.OrderItem;
import com.TempoLux.domain.port.OrderRepository;
import com.TempoLux.infrastructure.postgres.persistence.OrderItemR2dbcRepository;
import com.TempoLux.infrastructure.postgres.persistence.OrderR2dbcRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador R2DBC del puerto OrderRepository.
 * Persiste pedidos y sus líneas en una única transacción reactiva.
 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderR2dbcRepository r2dbcOrderRepository;
    private final OrderItemR2dbcRepository r2dbcOrderItemRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public Mono<Order> save(Order order) {
        return r2dbcOrderRepository.save(orderPersistenceMapper.toPersistence(order))
                .map(orderPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Order> findByOrderCode(String orderCode) {
        return r2dbcOrderRepository.findByOrderCode(orderCode)
                .map(orderPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Order> findById(Long id) {
        return r2dbcOrderRepository.findById(id)
                .map(orderPersistenceMapper::toDomain);
    }

    @Override
    public Flux<OrderItem> saveOrderItems(List<OrderItem> items, Long orderId) {
        if (items == null || items.isEmpty()) {
            return Flux.empty();
        }
        List<com.TempoLux.domain.entity.OrderItemEntity> itemsToSave = items.stream()
                .map(item -> orderPersistenceMapper.toPersistence(item, orderId))
                .toList();
        return r2dbcOrderItemRepository.saveAll(itemsToSave)
                .map(orderPersistenceMapper::toDomain);
    }

    @Override
    public Flux<OrderItem> findItemsByOrderId(Long orderId) {
        return r2dbcOrderItemRepository.findByOrderId(orderId)
                .map(orderPersistenceMapper::toDomain);
    }
}
