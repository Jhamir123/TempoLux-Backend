package com.TempoLux.domain.mappers;

import com.TempoLux.domain.dto.response.ProductColorOption;
import com.TempoLux.domain.model.Product;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

/**
 * Mapeo entre la entidad de dominio Product (model) y el modelo de persistencia R2DBC Product (entity).
 * Los listados tipados (colors, features) se serializan/deserializan a JSON.
 */
@Component
public class ProductPersistenceMapper {

    private static final TypeReference<List<ProductColorOption>> COLOR_LIST_TYPE = new TypeReference<>() {
    };
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public ProductPersistenceMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Product toDomain(com.TempoLux.domain.entity.ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return Product.builder()
                .id(entity.getId())
                .sku(entity.getSku())
                .name(entity.getName())
                .series(entity.getSeries())
                .tagline(entity.getTagline())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .diameter(entity.getDiameter())
                .caseMaterial(entity.getCaseMaterial())
                .bezel(entity.getBezel())
                .movement(entity.getMovement())
                .powerReserve(entity.getPowerReserve())
                .waterResistance(entity.getWaterResistance())
                .crystal(entity.getCrystal())
                .bracelet(entity.getBracelet())
                .modelPath(entity.getModelPath())
                .accentColor(entity.getAccentColor())
                .colorName(entity.getColorName())
                .colors(deserializeColors(entity.getColors()))
                .features(deserializeFeatures(entity.getFeatures()))
                .stock(entity.getStock())
                .active(Boolean.TRUE.equals(entity.getActive()))
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public com.TempoLux.domain.entity.ProductEntity toPersistence(Product product) {
        if (product == null) {
            return null;
        }
        return com.TempoLux.domain.entity.ProductEntity.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .series(product.getSeries())
                .tagline(product.getTagline())
                .category(product.getCategory())
                .description(product.getDescription())
                .price(product.getPrice())
                .diameter(product.getDiameter())
                .caseMaterial(product.getCaseMaterial())
                .bezel(product.getBezel())
                .movement(product.getMovement())
                .powerReserve(product.getPowerReserve())
                .waterResistance(product.getWaterResistance())
                .crystal(product.getCrystal())
                .bracelet(product.getBracelet())
                .modelPath(product.getModelPath())
                .accentColor(product.getAccentColor())
                .colorName(product.getColorName())
                .colors(serializeColors(product.getColors()))
                .features(serializeFeatures(product.getFeatures()))
                .stock(product.getStock())
                .active(product.isActive())
                .createdAt(product.getCreatedAt())
                .build();
    }

    private String serializeColors(List<ProductColorOption> colors) {
        if (colors == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(colors);
        } catch (JacksonException e) {
            return null;
        }
    }

    private String serializeFeatures(List<String> features) {
        if (features == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(features);
        } catch (JacksonException e) {
            return null;
        }
    }

    private List<ProductColorOption> deserializeColors(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, COLOR_LIST_TYPE);
        } catch (JacksonException e) {
            return Collections.emptyList();
        }
    }

    private List<String> deserializeFeatures(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, STRING_LIST_TYPE);
        } catch (JacksonException e) {
            return Collections.emptyList();
        }
    }
}
