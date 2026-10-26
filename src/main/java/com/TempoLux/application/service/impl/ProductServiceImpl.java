package com.TempoLux.application.service.impl;

import com.TempoLux.crosscutting.exception.ResourceNotFoundException;
import com.TempoLux.domain.dto.response.ProductDetailResponseDto;
import com.TempoLux.domain.dto.response.ProductResponseDto;
import com.TempoLux.domain.mappers.ProductDtoMapper;
import com.TempoLux.domain.model.Product;
import com.TempoLux.domain.port.ProductRepository;
import com.TempoLux.application.service.ProductService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Caso de uso de catálogo: listado, búsqueda/filtrado dinámico y detalle de producto.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    /**
     * Palabras vacías que se descartan al descomponer una consulta en términos de búsqueda.
     */
    private static final Set<String> STOP_WORDS = Set.of(
            "de", "del", "la", "las", "el", "los", "un", "una", "unos", "unas", "y", "o", "u",
            "en", "con", "para", "por", "que", "qué", "como", "cuál", "cual", "más", "mas",
            "es", "son", "lo", "al", "me", "mi", "te", "tu", "se", "su");

    private final ProductRepository productRepository;

    @Override
    public Flux<ProductResponseDto> searchProducts(String query, String category, BigDecimal maxPrice) {
        boolean hasFilters = StringUtils.hasText(query)
                || StringUtils.hasText(category)
                || maxPrice != null;

        if (!hasFilters) {
            return productRepository.findAll().map(ProductDtoMapper::toResponse);
        }

        Flux<Product> products = productRepository.findByFilters(query, category, maxPrice);

        if (StringUtils.hasText(query)) {
            List<String> terms = splitIntoTerms(query);
            if (terms.size() > 1) {
                products = products.switchIfEmpty(
                        Flux.defer(() -> productRepository.findByTerms(terms, category, maxPrice)));
            }
        }

        return products.map(ProductDtoMapper::toResponse);
    }

    /**
     * Descompone la consulta en términos relevantes (min. 3 caracteres, sin palabras vacías).
     * "Datejust 41 verde esmeralda" -&gt; [datejust, 41, verde, esmeralda]
     */
    private List<String> splitIntoTerms(String query) {
        return Arrays.stream(query.toLowerCase().split("[^\\p{L}\\p{N}]+"))
                .map(String::trim)
                .filter(term -> term.length() >= 3)
                .filter(term -> !STOP_WORDS.contains(term))
                .distinct()
                .toList();
    }

    @Override
    public Mono<ProductDetailResponseDto> getProductDetails(Long productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Producto no encontrado con id: " + productId)))
                .map(ProductDtoMapper::toDetailResponse);
    }
}
