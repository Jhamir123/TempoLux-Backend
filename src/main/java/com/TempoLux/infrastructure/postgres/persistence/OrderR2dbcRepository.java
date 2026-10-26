package com.TempoLux.infrastructure.postgres.persistence;

import com.TempoLux.domain.entity.OrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC no bloqueante para la tabla "orders".
 */
public interface OrderR2dbcRepository extends ReactiveCrudRepository<OrderEntity, Long> {

    Mono<OrderEntity> findByOrderCode(String orderCode);
}
