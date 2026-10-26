package com.TempoLux.infrastructure.influx;

import com.TempoLux.crosscutting.config.InfluxProperties;
import com.TempoLux.domain.model.telemetry.TemperatureTelemetry;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class InfluxTelemetryWriter {

    private static final Logger log = LoggerFactory.getLogger(InfluxTelemetryWriter.class);

    private final WriteApi writeApi;
    private final InfluxProperties properties;

    public InfluxTelemetryWriter(WriteApi writeApi, InfluxProperties properties) {
        this.writeApi = writeApi;
        this.properties = properties;
    }

    public void write(TemperatureTelemetry telemetry) {
        Point point = Point.measurement(properties.measurement())
                .addTag("dispositivo", telemetry.dispositivo())
                .addField("temperatura", telemetry.temperatura())
                .addField("humedad", telemetry.humedad())
                .time(Instant.now(), WritePrecision.NS);

        writeApi.writePoint(point);
        log.trace("Telemetría enviada a InfluxDB: {}", telemetry);
    }
}
