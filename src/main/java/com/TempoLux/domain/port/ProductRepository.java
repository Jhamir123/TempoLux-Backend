package com.TempoLux.domain.port;

import com.TempoLux.domain.model.Product;
import java.math.BigDecimal;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida para el catálogo de productos y el stock en tiempo real.
 */
public interface ProductRepository {

    Flux<Product> findAll();

    Flux<Product> findByFilters(String query, String category, BigDecimal maxPrice);

    /**
     * Búsqueda por términos independientes: el producto debe coincidir con TODOS los términos
     * (AND), pero cada término puede aparecer en cualquier campo indexable.
     * Se usa como segunda pasada cuando la búsqueda por frase completa no arroja resultados,
     * para que consultas naturales como "Datejust 41 verde esmeralda" sí encuentren el producto.
     */
    Flux<Product> findByTerms(List<String> terms, String category, BigDecimal maxPrice);

    Mono<Product> findById(Long id);

    /**
     * Decremento atómico del stock (UPDATE ... WHERE stock &gt;= :quantity).
     *
     * @return número de filas afectadas (1 si el producto tenía stock suficiente).
     */
    Mono<Integer> decrementStock(Long productId, int quantity);
}
