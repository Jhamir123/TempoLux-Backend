package com.TempoLux.api.controller;

import com.TempoLux.domain.dto.request.UserLoginRequestDto;
import com.TempoLux.domain.dto.request.UserRegisterRequestDto;
import com.TempoLux.domain.dto.response.UserAuthResponseDto;
import com.TempoLux.domain.dto.response.UserResponseDto;
import com.TempoLux.application.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthControllerTest {

    @Mock
    private UserAuthService authService;

    @InjectMocks
    private UserAuthController controller;

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "Juan Pérez",
                "juan@test.com",
                "password123",
                "Alta Relojería"
        );

        UserResponseDto response = new UserResponseDto(
                1L,
                "Juan Pérez",
                "juan@test.com",
                "TL-ABC123-CH",
                "Gold",
                "Alta Relojería",
                "Octubre 2024"
        );

        when(authService.register(any(UserRegisterRequestDto.class)))
                .thenReturn(Mono.just(response));

        Mono<UserResponseDto> result = controller.register(request);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    assertThat(dto.id()).isEqualTo(1L);
                    assertThat(dto.name()).isEqualTo("Juan Pérez");
                    assertThat(dto.email()).isEqualTo("juan@test.com");
                    return true;
                })
                .verifyComplete();

        verify(authService).register(request);
    }

    @Test
    void shouldLoginSuccessfully() {
        UserLoginRequestDto request = new UserLoginRequestDto("juan@test.com", "password123");

        UserResponseDto userDto = new UserResponseDto(
                1L,
                "Juan Pérez",
                "juan@test.com",
                "TL-ABC123-CH",
                "Gold",
                "Alta Relojería",
                "Octubre 2024"
        );

        UserAuthResponseDto response = new UserAuthResponseDto(
                "eyJhbGciOiJIUzI1NiJ9...",
                "Bearer",
                86400L,
                userDto
        );

        when(authService.login(any(UserLoginRequestDto.class)))
                .thenReturn(Mono.just(response));

        Mono<UserAuthResponseDto> result = controller.login(request);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    assertThat(dto.token()).isEqualTo("eyJhbGciOiJIUzI1NiJ9...");
                    assertThat(dto.tokenType()).isEqualTo("Bearer");
                    assertThat(dto.expiresIn()).isEqualTo(86400L);
                    assertThat(dto.user().email()).isEqualTo("juan@test.com");
                    return true;
                })
                .verifyComplete();

        verify(authService).login(request);
    }
}