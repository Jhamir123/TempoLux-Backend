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
 * Modelo de persistencia R2DBC que refleja la tabla "orders".
 * El status se persiste como el nombre del enum OrderStatus (PENDIENTE, EN_PREPARACION...).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class OrderEntity {

    @Id
    private Long id;

    @Column("order_code")
    private String orderCode;

    @Column("user_id")
    private Long userId;

    @Column("customer_name")
    private String customerName;

    @Column("customer_email")
    private String customerEmail;

    private String status;
    private BigDecimal total;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
