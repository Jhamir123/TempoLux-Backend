package com.TempoLux.infrastructure.postgres.persistence;

import com.TempoLux.domain.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Repositorio R2DBC no bloqueante para la tabla "users".
 */
public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, Long> {

    Mono<UserEntity> findByEmail(String email);
}
