package com.TempoLux.crosscutting.utils;

import com.TempoLux.crosscutting.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

/**
 * Utilidad reactiva de JWT: generación, validación y extracción de claims.
 * Usa HMAC-SHA con la clave configurada en jwt.secret.
 */
@Component
public class JwtUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = buildSigningKey(jwtProperties.getSecret());
    }

    /**
     * Acepta la clave como Base64 o como texto plano de al menos 32 bytes.
     */
    private SecretKey buildSigningKey(String secret) {
        try {
            byte[] decoded = Decoders.BASE64.decode(secret);
            if (decoded.length >= 32) {
                return Keys.hmacShaKeyFor(decoded);
            }
        } catch (Exception ignored) {
            // no es Base64: se evalúa el texto plano
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtProperties.getExpiration())))
                .signWith(signingKey)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return parse(token).getSubject();
    }

    public long getExpiration() {
        return jwtProperties.getExpiration();
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Token aleatorio de N caracteres alfanuméricos usado en el memberId de afiliados
     * (formato TL-XXXXXX-CH del frontend).
     */
    public static String randomCode(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (byte b : bytes) {
            sb.append(alphabet.charAt((b & 0xFF) % alphabet.length()));
        }
        return sb.toString();
    }
}
