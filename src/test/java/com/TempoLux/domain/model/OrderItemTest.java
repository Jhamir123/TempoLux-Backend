package com.TempoLux.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemWithAllFields() {
        OrderItem item = OrderItem.builder()
                .id(1L)
                .orderId(100L)
                .productId(10L)
                .productName("Rolex Submariner Date")
                .quantity(2)
                .unitPrice(new BigDecimal("8500.00"))
                .subtotal(new BigDecimal("17000.00"))
                .build();

        assertThat(item.getId()).isEqualTo(1L);
        assertThat(item.getOrderId()).isEqualTo(100L);
        assertThat(item.getProductId()).isEqualTo(10L);
        assertThat(item.getProductName()).isEqualTo("Rolex Submariner Date");
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getUnitPrice()).isEqualTo(new BigDecimal("8500.00"));
        assertThat(item.getSubtotal()).isEqualTo(new BigDecimal("17000.00"));
    }

    @Test
    void shouldCreateOrderItemWithMinimalFields() {
        OrderItem item = OrderItem.builder()
                .productId(5L)
                .productName("Omega Seamaster")
                .quantity(1)
                .unitPrice(new BigDecimal("4500.00"))
                .subtotal(new BigDecimal("4500.00"))
                .build();

        assertThat(item.getProductId()).isEqualTo(5L);
        assertThat(item.getProductName()).isEqualTo("Omega Seamaster");
        assertThat(item.getQuantity()).isEqualTo(1);
        assertThat(item.getUnitPrice()).isEqualTo(new BigDecimal("4500.00"));
        assertThat(item.getSubtotal()).isEqualTo(new BigDecimal("4500.00"));
        assertThat(item.getId()).isNull();
        assertThat(item.getOrderId()).isNull();
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        OrderItem item1 = OrderItem.builder().id(1L).productId(10L).build();
        OrderItem item2 = OrderItem.builder().id(1L).productId(10L).build();
        OrderItem item3 = OrderItem.builder().id(2L).productId(20L).build();

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
        assertThat(item1).isNotEqualTo(item3);
    }

    @Test
    void shouldSupportToString() {
        OrderItem item = OrderItem.builder()
                .id(1L)
                .productName("Test Product")
                .quantity(3)
                .build();

        String toString = item.toString();
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("productName=Test Product");
        assertThat(toString).contains("quantity=3");
    }
}