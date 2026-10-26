package com.TempoLux.api.controller;

import com.TempoLux.domain.dto.response.ProductDetailResponseDto;
import com.TempoLux.domain.dto.response.ProductResponseDto;
import com.TempoLux.application.service.ProductService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Endpoints reactivos del catálogo: listado con filtros dinámicos y detalle por id.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Flux<ProductResponseDto> list(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return productService.searchProducts(query, category, maxPrice);
    }

    @GetMapping("/{id}")
    public Mono<ProductDetailResponseDto> detail(@PathVariable Long id) {
        return productService.getProductDetails(id);
    }
}
