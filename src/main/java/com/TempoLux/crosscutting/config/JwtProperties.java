package com.TempoLux.crosscutting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades JWT (jwt.secret, jwt.expiration) definidas en application.yaml.
 */
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long expiration;
}
