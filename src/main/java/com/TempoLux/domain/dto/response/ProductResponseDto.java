package com.TempoLux.domain.dto.response;

import com.TempoLux.domain.dto.response.ProductColorOption;
import java.math.BigDecimal;
import java.util.List;

/**
 * Producto resumido del catálogo. priceNum (numérico) y price (formato "USD 12,450")
 * coinciden con la estructura que consume el frontend.
 */
public record ProductResponseDto(
        Long id,
        String sku,
        String name,
        String series,
        String tagline,
        String category,
        String description,
        String price,
        BigDecimal priceNum,
        String modelPath,
        String accentColor,
        String colorName,
        Integer stock,
        boolean available,
        List<ProductColorOption> colors,
        List<String> features) {
}
