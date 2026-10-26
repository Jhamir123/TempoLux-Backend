package com.TempoLux.api.controller;

import com.TempoLux.domain.dto.request.UserLoginRequestDto;
import com.TempoLux.domain.dto.request.UserRegisterRequestDto;
import com.TempoLux.domain.dto.response.UserAuthResponseDto;
import com.TempoLux.domain.dto.response.UserResponseDto;
import com.TempoLux.application.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Endpoints reactivos de autenticación: registro y login (JWT).
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponseDto> register(@Valid @RequestBody UserRegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Mono<UserAuthResponseDto> login(@Valid @RequestBody UserLoginRequestDto request) {
        return authService.login(request);
    }
}
