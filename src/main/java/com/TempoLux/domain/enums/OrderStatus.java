package com.TempoLux.domain.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Estados de envío y despacho de un pedido en TempoLux.
 * El valor persistido en base de datos es el nombre del enum (PENDIENTE, EN_PREPARACION...)
 * y la etiqueta en español es la que se expone al frontend y al chatbot.
 */
@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PENDIENTE("Pendiente"),
    EN_PREPARACION("En Preparación"),
    EN_TRANSITO("En Tránsito"),
    ENTREGADO("Entregado");

    private final String label;

    public static OrderStatus fromLabel(String label) {
        if (label == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(status -> status.getLabel().equalsIgnoreCase(label.trim()))
                .findFirst()
                .orElse(null);
    }

    public static OrderStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElse(null);
    }
}
