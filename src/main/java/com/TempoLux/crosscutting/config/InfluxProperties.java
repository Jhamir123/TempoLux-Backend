package com.TempoLux.crosscutting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "influx")
public record InfluxProperties(
        String url,
        String org,
        String bucket,
        String token,
        String measurement) {
}
