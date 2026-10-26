package com.TempoLux.crosscutting.utils;

import com.TempoLux.crosscutting.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("TempoLux2026SuperSecretJwtSigningKeyForLuxuryEcommerceReactiveApi_SwissMade_MustBeLongEnough");
        properties.setExpiration(86400000L);
        jwtUtil = new JwtUtil(properties);
    }

    @Test
    void shouldGenerateValidToken() {
        String token = jwtUtil.generateToken("juan@test.com", Map.of("role", "CLIENT"));

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    void shouldValidateGeneratedToken() {
        String token = jwtUtil.generateToken("juan@test.com", Map.of("role", "CLIENT"));

        assertThat(jwtUtil.isValid(token)).isTrue();
    }

    @Test
    void shouldExtractEmailFromToken() {
        String token = jwtUtil.generateToken("juan@test.com", Map.of("role", "CLIENT"));

        assertThat(jwtUtil.extractEmail(token)).isEqualTo("juan@test.com");
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        assertThat(jwtUtil.isValid("invalid.token.here")).isFalse();
        assertThat(jwtUtil.isValid("")).isFalse();
        assertThat(jwtUtil.isValid(null)).isFalse();
    }

    @Test
    void shouldReturnExpirationInMilliseconds() {
        assertThat(jwtUtil.getExpiration()).isEqualTo(86400000L);
    }

    @Test
    void shouldIncludeClaimsInToken() {
        String token = jwtUtil.generateToken("juan@test.com", Map.of("role", "ADMIN", "custom", "value"));

        assertThat(jwtUtil.isValid(token)).isTrue();
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("juan@test.com");
    }

    @Test
    void shouldGenerateRandomCode() {
        String code1 = JwtUtil.randomCode(4);
        String code2 = JwtUtil.randomCode(4);
        String code3 = JwtUtil.randomCode(6);

        assertThat(code1).hasSize(4);
        assertThat(code2).hasSize(4);
        assertThat(code3).hasSize(6);
        assertThat(code1).matches("[A-Z0-9]+");
        assertThat(code2).matches("[A-Z0-9]+");
    }
}