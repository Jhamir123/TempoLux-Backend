package com.TempoLux.domain.model;

import com.TempoLux.domain.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void shouldCreateOrderWithAllFields() {
        Order order = Order.builder()
                .id(1L)
                .orderCode("ORD-2026-ABC")
                .userId(10L)
                .customerName("Juan Pérez")
                .customerEmail("juan@test.com")
                .status(OrderStatus.PENDIENTE)
                .total(new BigDecimal("17000.00"))
                .createdAt(LocalDateTime.of(2026, 1, 15, 10, 30))
                .updatedAt(LocalDateTime.of(2026, 1, 15, 10, 30))
                .build();

        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getOrderCode()).isEqualTo("ORD-2026-ABC");
        assertThat(order.getUserId()).isEqualTo(10L);
        assertThat(order.getCustomerName()).isEqualTo("Juan Pérez");
        assertThat(order.getCustomerEmail()).isEqualTo("juan@test.com");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDIENTE);
        assertThat(order.getTotal()).isEqualTo(new BigDecimal("17000.00"));
        assertThat(order.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 15, 10, 30));
        assertThat(order.getUpdatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 15, 10, 30));
    }

    @Test
    void shouldCreateOrderWithMinimalFields() {
        Order order = Order.builder()
                .orderCode("ORD-2026-XYZ")
                .userId(5L)
                .customerName("María García")
                .customerEmail("maria@test.com")
                .status(OrderStatus.ENTREGADO)
                .total(new BigDecimal("5200.00"))
                .build();

        assertThat(order.getOrderCode()).isEqualTo("ORD-2026-XYZ");
        assertThat(order.getUserId()).isEqualTo(5L);
        assertThat(order.getCustomerName()).isEqualTo("María García");
        assertThat(order.getCustomerEmail()).isEqualTo("maria@test.com");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ENTREGADO);
        assertThat(order.getTotal()).isEqualTo(new BigDecimal("5200.00"));
        assertThat(order.getId()).isNull();
        assertThat(order.getCreatedAt()).isNull();
        assertThat(order.getUpdatedAt()).isNull();
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        Order order1 = Order.builder().id(1L).orderCode("ORD-2026-ABC").build();
        Order order2 = Order.builder().id(1L).orderCode("ORD-2026-ABC").build();
        Order order3 = Order.builder().id(2L).orderCode("ORD-2026-XYZ").build();

        assertThat(order1).isEqualTo(order2);
        assertThat(order1.hashCode()).isEqualTo(order2.hashCode());
        assertThat(order1).isNotEqualTo(order3);
    }

    @Test
    void shouldSupportToString() {
        Order order = Order.builder()
                .id(1L)
                .orderCode("ORD-2026-TEST")
                .customerName("Test Customer")
                .build();

        String toString = order.toString();
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("orderCode=ORD-2026-TEST");
        assertThat(toString).contains("customerName=Test Customer");
    }
}