package com.TempoLux.crosscutting.config;

import com.TempoLux.domain.enums.UserRole;
import com.TempoLux.domain.model.User;
import com.TempoLux.domain.port.UserRepository;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Siembra el socio demo de TempoLux (vip@TempoLux.ch / password123) con la
 * contraseña codificada en BCrypt para poder probar el login.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private static final String DEMO_EMAIL = "vip@tempolux.ch";
    private static final String DEMO_PASSWORD = "password123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.findByEmail(DEMO_EMAIL)
                .flatMap(user -> DEMO_EMAIL.equalsIgnoreCase(user.getEmail())
                        ? Mono.just(user)
                        : userRepository.findById(user.getId())
                                .map(current -> User.builder()
                                        .id(current.getId())
                                        .name(current.getName())
                                        .email(DEMO_EMAIL)
                                        .password(current.getPassword())
                                        .memberId(current.getMemberId())
                                        .tier(current.getTier())
                                        .preferredCollection(current.getPreferredCollection())
                                        .role(current.getRole())
                                        .joinedDate(current.getJoinedDate())
                                        .createdAt(current.getCreatedAt())
                                        .build())
                                .flatMap(userRepository::save))
                .switchIfEmpty(Mono.defer(this::createDemoUser))
                .subscribe(
                        user -> log.info("Socio demo listo: {}", user.getEmail()),
                        error -> log.warn("No se pudo sembrar el socio demo (¿BD no disponible?): {}", error.getMessage()));
    }

    private Mono<User> createDemoUser() {
        User demo = User.builder()
                .name("Alexander Vance")
                .email(DEMO_EMAIL)
                .password(passwordEncoder.encode(DEMO_PASSWORD))
                .memberId("TL-8842-CH")
                .tier("Patrón Esmeralda VIP")
                .preferredCollection("Oyster Perpetual Datejust")
                .role(UserRole.VIP)
                .joinedDate(currentMonthYearSpanish())
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(demo);
    }

    private String currentMonthYearSpanish() {
        String formatted = DateTimeFormatter
                .ofPattern("MMMM yyyy", new Locale("es", "PE"))
                .format(LocalDate.now());
        return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
    }
}
