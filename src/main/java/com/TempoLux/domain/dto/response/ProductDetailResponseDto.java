package com.TempoLux.domain.dto.response;

import com.TempoLux.domain.dto.response.ProductColorOption;
import java.math.BigDecimal;
import java.util.List;

/**
 * Detalle completo de un reloj: además de la información del catálogo incluye las
 * especificaciones técnicas (caja, bisel, movimiento, reserva de marcha, resistencia
 * al agua, cristal, correa) y el stock actual en tiempo real.
 */
public record ProductDetailResponseDto(
        Long id,
        String sku,
        String name,
        String series,
        String tagline,
        String category,
        String description,
        String price,
        BigDecimal priceNum,
        String diameter,
        String caseMaterial,
        String bezel,
        String movement,
        String powerReserve,
        String waterResistance,
        String crystal,
        String bracelet,
        String modelPath,
        String accentColor,
        String colorName,
        Integer stock,
        boolean available,
        List<ProductColorOption> colors,
        List<String> features) {
}
