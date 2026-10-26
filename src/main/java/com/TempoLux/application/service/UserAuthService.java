package com.TempoLux.application.service;

import com.TempoLux.domain.dto.request.UserLoginRequestDto;
import com.TempoLux.domain.dto.request.UserRegisterRequestDto;
import com.TempoLux.domain.dto.response.UserAuthResponseDto;
import com.TempoLux.domain.dto.response.UserResponseDto;
import reactor.core.publisher.Mono;

/**
 * Servicio de autenticación y gestión de clientes.
 * Contrato de la capa de aplicación; implementado en application.service.
 */
public interface UserAuthService {

    Mono<UserResponseDto> register(UserRegisterRequestDto request);

    Mono<UserAuthResponseDto> login(UserLoginRequestDto request);
}
