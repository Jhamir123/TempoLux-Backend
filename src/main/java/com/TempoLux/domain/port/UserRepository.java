package com.TempoLux.domain.port;

import com.TempoLux.domain.model.User;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para persistir y consultar usuarios.
 */
public interface UserRepository {

    Mono<User> findByEmail(String email);

    Mono<User> findById(Long id);

    Mono<User> save(User user);
}
