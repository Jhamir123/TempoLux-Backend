package com.TempoLux.application.service.impl;

import com.TempoLux.crosscutting.exception.DuplicateResourceException;
import com.TempoLux.crosscutting.exception.InvalidCredentialsException;
import com.TempoLux.crosscutting.utils.JwtUtil;
import com.TempoLux.domain.constans.ApplicationConstants;
import com.TempoLux.domain.dto.request.UserLoginRequestDto;
import com.TempoLux.domain.dto.request.UserRegisterRequestDto;
import com.TempoLux.domain.dto.response.UserAuthResponseDto;
import com.TempoLux.domain.dto.response.UserResponseDto;
import com.TempoLux.domain.enums.UserRole;
import com.TempoLux.domain.mappers.UserDtoMapper;
import com.TempoLux.domain.model.User;
import com.TempoLux.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private UserAuthServiceImpl userAuthService;

    @BeforeEach
    void setUp() {
        userAuthService = new UserAuthServiceImpl(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void shouldRegisterNewUserSuccessfully() {
        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "Juan Pérez",
                "juan@test.com",
                "password123",
                "Alta Relojería"
        );

        User savedUser = User.builder()
                .id(1L)
                .name("Juan Pérez")
                .email("juan@test.com")
                .password("encodedPassword")
                .memberId("TL-ABC123-CH")
                .tier(ApplicationConstants.DEFAULT_TIER)
                .preferredCollection("Alta Relojería")
                .role(UserRole.CLIENT)
                .joinedDate("Octubre 2024")
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("juan@test.com")).thenReturn(Mono.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        StepVerifier.create(userAuthService.register(request))
                .expectNextMatches(response -> {
                    assertThat(response.id()).isEqualTo(1L);
                    assertThat(response.name()).isEqualTo("Juan Pérez");
                    assertThat(response.email()).isEqualTo("juan@test.com");
                    assertThat(response.memberId()).isEqualTo("TL-ABC123-CH");
                    return true;
                })
                .verifyComplete();

        verify(userRepository).findByEmail("juan@test.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenEmailExists() {
        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "Juan Pérez",
                "juan@test.com",
                "password123",
                "Alta Relojería"
        );

        User existingUser = User.builder().id(1L).email("juan@test.com").build();

        when(userRepository.findByEmail("juan@test.com")).thenReturn(Mono.just(existingUser));

        StepVerifier.create(userAuthService.register(request))
                .expectError(DuplicateResourceException.class)
                .verify();

        verify(userRepository).findByEmail("juan@test.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNormalizeEmailToLowerCase() {
        UserRegisterRequestDto request = new UserRegisterRequestDto(
                "Juan Pérez",
                "  JUAN@TEST.COM  ",
                "password123",
                null
        );

        User savedUser = User.builder()
                .id(1L)
                .name("Juan Pérez")
                .email("juan@test.com")
                .build();

        when(userRepository.findByEmail("juan@test.com")).thenReturn(Mono.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        StepVerifier.create(userAuthService.register(request))
                .expectNextMatches(response -> response.email().equals("juan@test.com"))
                .verifyComplete();

        verify(userRepository).findByEmail("juan@test.com");
    }

    @Test
    void shouldLoginSuccessfully() {
        UserLoginRequestDto request = new UserLoginRequestDto("juan@test.com", "password123");

        User user = User.builder()
                .id(1L)
                .name("Juan Pérez")
                .email("juan@test.com")
                .password("encodedPassword")
                .memberId("TL-ABC123-CH")
                .tier("Gold")
                .preferredCollection("Alta Relojería")
                .role(UserRole.CLIENT)
                .joinedDate("Octubre 2024")
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("juan@test.com")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), any(Map.class))).thenReturn("jwt-token-123");
        when(jwtUtil.getExpiration()).thenReturn(86400000L);

        StepVerifier.create(userAuthService.login(request))
                .expectNextMatches(response -> {
                    assertThat(response.token()).isEqualTo("jwt-token-123");
                    assertThat(response.tokenType()).isEqualTo("Bearer");
                    assertThat(response.expiresIn()).isEqualTo(86400L);
                    assertThat(response.user().email()).isEqualTo("juan@test.com");
                    return true;
                })
                .verifyComplete();

        verify(userRepository).findByEmail("juan@test.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil).generateToken(anyString(), any(Map.class));
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenUserNotFound() {
        UserLoginRequestDto request = new UserLoginRequestDto("nonexistent@test.com", "password123");

        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Mono.empty());

        StepVerifier.create(userAuthService.login(request))
                .expectError(InvalidCredentialsException.class)
                .verify();

        verify(userRepository).findByEmail("nonexistent@test.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenPasswordMismatch() {
        UserLoginRequestDto request = new UserLoginRequestDto("juan@test.com", "wrongpassword");

        User user = User.builder()
                .id(1L)
                .email("juan@test.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail("juan@test.com")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        StepVerifier.create(userAuthService.login(request))
                .expectError(InvalidCredentialsException.class)
                .verify();

        verify(userRepository).findByEmail("juan@test.com");
        verify(passwordEncoder).matches("wrongpassword", "encodedPassword");
    }

    @Test
    void shouldNormalizeEmailInLogin() {
        UserLoginRequestDto request = new UserLoginRequestDto("  JUAN@TEST.COM  ", "password123");

        User user = User.builder().id(1L).email("juan@test.com").password("encoded").role(UserRole.CLIENT).build();

        when(userRepository.findByEmail("juan@test.com")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("password123", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), any(Map.class))).thenReturn("token");
        when(jwtUtil.getExpiration()).thenReturn(86400000L);

        StepVerifier.create(userAuthService.login(request))
                .expectNextMatches(response -> response.token().equals("token"))
                .verifyComplete();

        verify(userRepository).findByEmail("juan@test.com");
    }
}