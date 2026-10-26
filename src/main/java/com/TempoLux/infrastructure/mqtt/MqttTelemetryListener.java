package com.TempoLux.infrastructure.mqtt;

import com.TempoLux.crosscutting.config.MqttProperties;
import com.TempoLux.domain.model.telemetry.TemperatureTelemetry;
import com.TempoLux.infrastructure.influx.InfluxTelemetryWriter;
import tools.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@EnableConfigurationProperties(MqttProperties.class)
public class MqttTelemetryListener implements MqttCallbackExtended {

    private static final Logger log = LoggerFactory.getLogger(MqttTelemetryListener.class);

    private final MqttProperties properties;
    private final ObjectMapper objectMapper;
    private final InfluxTelemetryWriter telemetryWriter;
    private MqttAsyncClient client;

    public MqttTelemetryListener(
            MqttProperties properties,
            ObjectMapper objectMapper,
            InfluxTelemetryWriter telemetryWriter) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.telemetryWriter = telemetryWriter;
    }

    @PostConstruct
    public void start() throws MqttException {
        String clientId = properties.clientId() + "-" + UUID.randomUUID();
        client = new MqttAsyncClient(properties.brokerUrl(), clientId, new MemoryPersistence());
        client.setCallback(this);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(properties.connectionTimeoutSeconds());
        options.setKeepAliveInterval(properties.keepAliveIntervalSeconds());

        client.connect(options, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                log.info("Conexión MQTT establecida con {}", properties.brokerUrl());
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                log.error("No se pudo conectar al broker MQTT {}", properties.brokerUrl(), exception);
            }
        });
    }

    private void subscribe() {
        try {
            client.subscribe(properties.topic(), properties.qos());
            log.info("MQTT suscrito a {} en {}", properties.topic(), properties.brokerUrl());
        } catch (MqttException exception) {
            log.error("No se pudo suscribir al tópico MQTT {}", properties.topic(), exception);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        try {
            TemperatureTelemetry telemetry = objectMapper.readValue(
                    message.getPayload(), TemperatureTelemetry.class);

            if (!telemetry.isValid()) {
                log.warn("Payload MQTT inválido en {}: {}", topic,
                        new String(message.getPayload(), StandardCharsets.UTF_8));
                return;
            }

            // El callback no espera a InfluxDB: WriteApi encola y escribe en segundo plano.
            telemetryWriter.write(telemetry);
        } catch (Exception exception) {
            log.warn("No se pudo procesar el mensaje MQTT del tópico {}", topic, exception);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        log.warn("Conexión MQTT perdida; Paho intentará reconectar automáticamente", cause);
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if (reconnect) {
            log.info("MQTT reconectado a {}; restaurando suscripción", serverURI);
        }
        subscribe();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Este backend solo consume mensajes.
    }

    @PreDestroy
    public void stop() {
        if (client == null) {
            return;
        }
        try {
            if (client.isConnected()) {
                client.disconnect().waitForCompletion(5_000);
            }
            client.close();
        } catch (MqttException exception) {
            log.warn("Error cerrando el cliente MQTT", exception);
        }
    }
}
