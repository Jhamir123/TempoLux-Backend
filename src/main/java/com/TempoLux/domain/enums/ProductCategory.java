package com.TempoLux.domain.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Categorías del catálogo TempoLux.
 * Se persisten por su etiqueta en español para que el filtro del frontend
 * (?category=Alta Relojería) coincida directamente con el valor almacenado.
 */
@Getter
@RequiredArgsConstructor
public enum ProductCategory {

    ALTA_RELOJERIA("Alta Relojería"),
    DEPORTIVO("Deportivo"),
    ARTESANAL_3D("Artesanal 3D");

    private final String label;

    public static ProductCategory fromLabel(String label) {
        if (label == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(category -> category.getLabel().equalsIgnoreCase(label.trim()))
                .findFirst()
                .orElse(null);
    }
}
