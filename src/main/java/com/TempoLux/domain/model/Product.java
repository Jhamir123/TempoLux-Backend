package com.TempoLux.domain.model;

import com.TempoLux.domain.dto.response.ProductColorOption;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de dominio del catálogo de relojes TempoLux.
 * La categoría se conserva como texto (etiqueta) para que coincida con la UI
 * y con el filtro dinámico del endpoint GET /api/v1/products.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;
    private String sku;
    private String name;
    private String series;
    private String tagline;
    private String category;
    private String description;
    private BigDecimal price;
    private String diameter;
    private String caseMaterial;
    private String bezel;
    private String movement;
    private String powerReserve;
    private String waterResistance;
    private String crystal;
    private String bracelet;
    private String modelPath;
    private String accentColor;
    private String colorName;
    private List<ProductColorOption> colors;
    private List<String> features;
    private Integer stock;
    private boolean active;
    private LocalDateTime createdAt;

    public boolean isAvailable() {
        return active && stock != null && stock > 0;
    }
}
