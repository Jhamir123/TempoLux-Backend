package com.TempoLux;

import com.TempoLux.crosscutting.config.JwtProperties;
import com.TempoLux.crosscutting.config.GroqProperties;
import com.TempoLux.crosscutting.utils.JwtUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TempoLuxTestConfig {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public JwtProperties jwtProperties() {
        JwtProperties props = new JwtProperties();
        props.setSecret("TempoLux2026SuperSecretJwtSigningKeyForLuxuryEcommerceReactiveApi_SwissMade_MustBeLongEnough");
        props.setExpiration(86400000L);
        return props;
    }

    @Bean
    @Primary
    public JwtUtil jwtUtil(JwtProperties jwtProperties) {
        return new JwtUtil(jwtProperties);
    }

    @Bean
    @Primary
    public GroqProperties groqProperties() {
        GroqProperties props = new GroqProperties();
        props.setModel("test-model");
        GroqProperties.Api api = new GroqProperties.Api();
        api.setKey("test-key");
        api.setUrl("https://api.groq.com/test");
        props.setApi(api);
        return props;
    }
}