package com.TempoLux.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Modelo de persistencia R2DBC que refleja la tabla "products".
 * Las columnas colors y features son textos JSON serializados por el ProductPersistenceMapper.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("products")
public class ProductEntity {

    @Id
    private Long id;

    private String sku;
    private String name;
    private String series;
    private String tagline;
    private String category;
    private String description;
    private BigDecimal price;
    private String diameter;

    @Column("case_material")
    private String caseMaterial;

    private String bezel;
    private String movement;

    @Column("power_reserve")
    private String powerReserve;

    @Column("water_resistance")
    private String waterResistance;

    private String crystal;
    private String bracelet;

    @Column("model_path")
    private String modelPath;

    @Column("accent_color")
    private String accentColor;

    @Column("color_name")
    private String colorName;

    private String colors;
    private String features;
    private Integer stock;
    private Boolean active;

    @Column("created_at")
    private LocalDateTime createdAt;
}
