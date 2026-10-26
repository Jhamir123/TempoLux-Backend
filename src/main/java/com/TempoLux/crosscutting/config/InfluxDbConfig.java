package com.TempoLux.crosscutting.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InfluxProperties.class)
public class InfluxDbConfig {

    @Bean(destroyMethod = "close")
    public InfluxDBClient influxDBClient(InfluxProperties properties) {
        return InfluxDBClientFactory.create(
                properties.url(),
                properties.token().toCharArray(),
                properties.org(),
                properties.bucket());
    }

    @Bean(destroyMethod = "close")
    public WriteApi influxWriteApi(InfluxDBClient client) {
        WriteOptions options = WriteOptions.builder()
                .batchSize(1)
                .flushInterval(20)
                .jitterInterval(0)
                .bufferLimit(10_000)
                .build();

        return client.makeWriteApi(options);
    }
}
