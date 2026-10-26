package com.TempoLux.infrastructure.postgres.persistence;

import com.TempoLux.domain.entity.OrderItemEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Repositorio R2DBC no bloqueante para la tabla "order_items".
 */
public interface OrderItemR2dbcRepository extends ReactiveCrudRepository<OrderItemEntity, Long> {

    Flux<OrderItemEntity> findByOrderId(Long orderId);
}
