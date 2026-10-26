package com.TempoLux.infrastructure.postgres.persistence;

import com.TempoLux.domain.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC no bloqueante para la tabla "products".
 * Incluye el decremento atómico de stock usado al confirmar pedidos.
 */
public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, Long> {

    @Modifying
    @Query("UPDATE products SET stock = stock - :quantity WHERE id = :id AND stock >= :quantity")
    Mono<Integer> decrementStock(@Param("id") Long id, @Param("quantity") int quantity);
}
