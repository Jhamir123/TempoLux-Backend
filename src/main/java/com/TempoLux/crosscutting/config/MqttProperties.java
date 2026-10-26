package com.TempoLux.crosscutting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mqtt")
public record MqttProperties(
        String brokerUrl,
        String topic,
        String clientId,
        int qos,
        int connectionTimeoutSeconds,
        int keepAliveIntervalSeconds) {
}
