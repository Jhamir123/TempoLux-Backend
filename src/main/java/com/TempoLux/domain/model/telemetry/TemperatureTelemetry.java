package com.TempoLux.domain.model.telemetry;

public record TemperatureTelemetry(
        String dispositivo,
        Double temperatura,
        Double humedad) {

    public boolean isValid() {
        return dispositivo != null
                && !dispositivo.isBlank()
                && temperatura != null
                && humedad != null
                && Double.isFinite(temperatura)
                && Double.isFinite(humedad);
    }
}
