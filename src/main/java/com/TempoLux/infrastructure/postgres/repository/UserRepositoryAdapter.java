package com.TempoLux.infrastructure.postgres.repository;

import com.TempoLux.domain.mappers.UserPersistenceMapper;
import com.TempoLux.domain.model.User;
import com.TempoLux.domain.port.UserRepository;
import com.TempoLux.infrastructure.postgres.persistence.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Adaptador R2DBC del puerto UserRepository (arquitectura hexagonal).
 * Traduce entre la entidad de dominio y el modelo de persistencia.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserR2dbcRepository r2dbcUserRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public Mono<User> findByEmail(String email) {
        return r2dbcUserRepository.findByEmail(email).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Mono<User> findById(Long id) {
        return r2dbcUserRepository.findById(id).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Mono<User> save(User user) {
        return r2dbcUserRepository.save(userPersistenceMapper.toPersistence(user))
                .map(userPersistenceMapper::toDomain);
    }
}
