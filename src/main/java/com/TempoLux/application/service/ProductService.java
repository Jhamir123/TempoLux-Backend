package com.TempoLux.application.service;

import com.TempoLux.domain.dto.response.ProductDetailResponseDto;
import com.TempoLux.domain.dto.response.ProductResponseDto;
import java.math.BigDecimal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Servicio de catálogo y disponibilidad de productos.
 * Contrato de la capa de aplicación; implementado en application.service.
 */
public interface ProductService {

    Flux<ProductResponseDto> searchProducts(String query, String category, BigDecimal maxPrice);

    Mono<ProductDetailResponseDto> getProductDetails(Long productId);
}
