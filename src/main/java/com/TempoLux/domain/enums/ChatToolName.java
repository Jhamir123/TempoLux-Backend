package com.TempoLux.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Nombre exacto (wire name) de las 3 herramientas que LuxBot expone
 * a la API de Groq mediante Tool Calling. Deben coincidir al pie de la letra
 * con las definiciones de funciones enviadas en la petición.
 */
@Getter
@RequiredArgsConstructor
public enum ChatToolName {

    SEARCH_PRODUCTS("search_products"),
    GET_PRODUCT_DETAILS("get_product_details"),
    GET_ORDER_STATUS("get_order_status");

    private final String functionName;

    public static ChatToolName fromFunctionName(String name) {
        for (ChatToolName tool : values()) {
            if (tool.getFunctionName().equals(name)) {
                return tool;
            }
        }
        return null;
    }
}
