package com.TempoLux.domain.mappers;

import com.TempoLux.domain.dto.response.ProductDetailResponseDto;
import com.TempoLux.domain.dto.response.ProductResponseDto;
import com.TempoLux.domain.model.Product;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Mapeo entre la entidad de dominio Product y los DTOs del catálogo.
 * priceNum conserva el valor numérico y price usa el formato "USD 12,450"
 * que muestra el frontend.
 */
public final class ProductDtoMapper {

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,##0");

    private ProductDtoMapper() {
    }

    public static ProductResponseDto toResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponseDto(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getSeries(),
                product.getTagline(),
                product.getCategory(),
                product.getDescription(),
                formatPrice(product.getPrice()),
                product.getPrice(),
                product.getModelPath(),
                product.getAccentColor(),
                product.getColorName(),
                product.getStock(),
                product.isAvailable(),
                product.getColors(),
                product.getFeatures());
    }

    public static ProductDetailResponseDto toDetailResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDetailResponseDto(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getSeries(),
                product.getTagline(),
                product.getCategory(),
                product.getDescription(),
                formatPrice(product.getPrice()),
                product.getPrice(),
                product.getDiameter(),
                product.getCaseMaterial(),
                product.getBezel(),
                product.getMovement(),
                product.getPowerReserve(),
                product.getWaterResistance(),
                product.getCrystal(),
                product.getBracelet(),
                product.getModelPath(),
                product.getAccentColor(),
                product.getColorName(),
                product.getStock(),
                product.isAvailable(),
                product.getColors(),
                product.getFeatures());
    }

    public static String formatPrice(BigDecimal price) {
        return "USD " + PRICE_FORMAT.format(price);
    }
}
