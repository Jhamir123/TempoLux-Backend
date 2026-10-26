package com.TempoLux.crosscutting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de la API de Groq (groq.api.key, groq.api.url, groq.model)
 * definidas en application.yaml.
 */
@Data
@ConfigurationProperties(prefix = "groq")
public class GroqProperties {

    private Api api;
    private String model;

    @Data
    public static class Api {

        private String key;
        private String url;
    }
}
