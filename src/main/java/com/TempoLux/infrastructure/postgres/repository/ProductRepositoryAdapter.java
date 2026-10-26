package com.TempoLux.infrastructure.postgres.repository;

import com.TempoLux.domain.mappers.ProductPersistenceMapper;
import com.TempoLux.domain.model.Product;
import com.TempoLux.domain.port.ProductRepository;
import com.TempoLux.infrastructure.postgres.persistence.ProductR2dbcRepository;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Adaptador R2DBC del puerto ProductRepository.
 * La búsqueda con filtros dinámicos (query / category / max_price) se construye
 * de forma segura con parámetros nombrados sobre PostgreSQL.
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductR2dbcRepository r2dbcProductRepository;
    private final ProductPersistenceMapper productPersistenceMapper;
    private final DatabaseClient databaseClient;

    @Override
    public Flux<Product> findAll() {
        return r2dbcProductRepository.findAll()
                .filter(entity -> Boolean.TRUE.equals(entity.getActive()))
                .map(productPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Product> findByFilters(String query, String category, BigDecimal maxPrice) {
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE active = TRUE");
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasText(category)) {
            sql.append(" AND category = :category");
            params.put("category", category.trim());
        }
        if (maxPrice != null) {
            sql.append(" AND price <= :maxPrice");
            params.put("maxPrice", maxPrice);
        }
        if (StringUtils.hasText(query)) {
            sql.append(" AND (").append(anyFieldMatch("query")).append(")");
            params.put("query", query.trim());
        }
        sql.append(" ORDER BY id ASC");

        return execute(sql, params);
    }

    @Override
    public Flux<Product> findByTerms(List<String> terms, String category, BigDecimal maxPrice) {
        if (terms == null || terms.isEmpty()) {
            return Flux.empty();
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE active = TRUE");
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasText(category)) {
            sql.append(" AND category = :category");
            params.put("category", category.trim());
        }
        if (maxPrice != null) {
            sql.append(" AND price <= :maxPrice");
            params.put("maxPrice", maxPrice);
        }

        for (int i = 0; i < terms.size(); i++) {
            String param = "term" + i;
            sql.append(" AND (").append(anyFieldMatch(param)).append(")");
            params.put(param, terms.get(i));
        }
        sql.append(" ORDER BY id ASC");

        return execute(sql, params);
    }

    /**
     * Condición SQL reutilizable: el parárafo debe aparecer en alguno de los campos
     * por los que el cliente puede describir un reloj (incluye color y características).
     */
    private String anyFieldMatch(String paramName) {
        return " LOWER(name) LIKE '%' || LOWER(:" + paramName + ") || '%'"
                + " OR LOWER(description) LIKE '%' || LOWER(:" + paramName + ") || '%'"
                + " OR LOWER(series) LIKE '%' || LOWER(:" + paramName + ") || '%'"
                + " OR LOWER(sku) LIKE '%' || LOWER(:" + paramName + ") || '%'"
                + " OR LOWER(color_name) LIKE '%' || LOWER(:" + paramName + ") || '%'"
                + " OR LOWER(tagline) LIKE '%' || LOWER(:" + paramName + ") || '%'"
                + " OR LOWER(features) LIKE '%' || LOWER(:" + paramName + ") || '%' ";
    }

    private Flux<Product> execute(StringBuilder sql, Map<String, Object> params) {
        var spec = databaseClient.sql(sql.toString());
        for (Map.Entry<String, Object> param : params.entrySet()) {
            spec = spec.bind(param.getKey(), param.getValue());
        }
        return spec.map((row, metadata) -> mapRow(row))
                .all()
                .map(productPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return r2dbcProductRepository.findById(id).map(productPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Integer> decrementStock(Long productId, int quantity) {
        return r2dbcProductRepository.decrementStock(productId, quantity);
    }

    private com.TempoLux.domain.entity.ProductEntity mapRow(Row row) {
        return com.TempoLux.domain.entity.ProductEntity.builder()
                .id(row.get("id", Long.class))
                .sku(row.get("sku", String.class))
                .name(row.get("name", String.class))
                .series(row.get("series", String.class))
                .tagline(row.get("tagline", String.class))
                .category(row.get("category", String.class))
                .description(row.get("description", String.class))
                .price(row.get("price", BigDecimal.class))
                .diameter(row.get("diameter", String.class))
                .caseMaterial(row.get("case_material", String.class))
                .bezel(row.get("bezel", String.class))
                .movement(row.get("movement", String.class))
                .powerReserve(row.get("power_reserve", String.class))
                .waterResistance(row.get("water_resistance", String.class))
                .crystal(row.get("crystal", String.class))
                .bracelet(row.get("bracelet", String.class))
                .modelPath(row.get("model_path", String.class))
                .accentColor(row.get("accent_color", String.class))
                .colorName(row.get("color_name", String.class))
                .colors(row.get("colors", String.class))
                .features(row.get("features", String.class))
                .stock(row.get("stock", Integer.class))
                .active(row.get("active", Boolean.class))
                .createdAt(row.get("created_at", LocalDateTime.class))
                .build();
    }
}
