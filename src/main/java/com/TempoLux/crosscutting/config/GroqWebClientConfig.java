package com.TempoLux.crosscutting.config;

import com.TempoLux.domain.constans.ApplicationConstants;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configura el WebClient reactivo que se comunica con la API de Groq.
 * La clave se inyecta como Bearer token desde el servidor (nunca en el navegador).
 */
@Configuration
@EnableConfigurationProperties(GroqProperties.class)
public class GroqWebClientConfig {

    @Bean(name = ApplicationConstants.GROQ_WEB_CLIENT_BEAN)
    public WebClient groqWebClient(GroqProperties groqProperties) {
        return WebClient.builder()
                .baseUrl(groqProperties.getApi().getUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + groqProperties.getApi().getKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
