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
import com.TempoLux.application.service.UserAuthService;
import com.TempoLux.domain.port.UserRepository;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * Caso de uso de autenticación: registro (BCrypt) y login (JWT reactivo).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private static final ZoneId APPLICATION_TIME_ZONE = ZoneId.of("America/Lima");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<UserResponseDto> register(UserRegisterRequestDto request) {
        String email = normalizeEmail(request.email());
        return userRepository.findByEmail(email)
                .hasElement()
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicateResourceException(
                                "Este correo electrónico ya cuenta con una membresía registrada."));
                    }
                    User newUser = buildNewUser(request, email);
                    return userRepository.save(newUser).map(UserDtoMapper::toResponse);
                });
    }

    @Override
    public Mono<UserAuthResponseDto> login(UserLoginRequestDto request) {
        String email = normalizeEmail(request.email());
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException(
                        "No existe una cuenta VIP vinculada a este correo electrónico.")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                        return Mono.error(new InvalidCredentialsException(
                                "La credencial o contraseña introducida es incorrecta."));
                    }
                    String token = jwtUtil.generateToken(
                            user.getEmail(),
                            Map.of(ApplicationConstants.CLAIM_ROLE, user.getRole().name()));
                    return Mono.just(new UserAuthResponseDto(
                            token,
                            "Bearer",
                            jwtUtil.getExpiration() / 1000,
                            UserDtoMapper.toResponse(user)));
                });
    }

    private User buildNewUser(UserRegisterRequestDto request, String email) {
        UserRole role = UserRole.CLIENT;
        return User.builder()
                .name(request.name().trim())
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .memberId(ApplicationConstants.MEMBER_ID_PREFIX
                        + JwtUtil.randomCode(4)
                        + ApplicationConstants.MEMBER_ID_SUFFIX)
                .tier(ApplicationConstants.DEFAULT_TIER)
                .preferredCollection(StringUtils.hasText(request.preferredCollection())
                        ? request.preferredCollection().trim()
                        : ApplicationConstants.DEFAULT_PREFERRED_COLLECTION)
                .role(role)
                .joinedDate(currentMonthYearSpanish())
                .createdAt(LocalDateTime.now(APPLICATION_TIME_ZONE))
                .build();
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String currentMonthYearSpanish() {
        String formatted = DateTimeFormatter
                .ofPattern("MMMM yyyy", new Locale("es", "PE"))
                .format(LocalDate.now(APPLICATION_TIME_ZONE));
        return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
    }
}
