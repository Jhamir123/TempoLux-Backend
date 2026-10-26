package com.TempoLux.domain.dto.request;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemRequestDtoTest {

    @Test
    void shouldCreateOrderItemRequestDtoWithAllFields() {
        OrderItemRequestDto dto = new OrderItemRequestDto(10L, 2, new BigDecimal("8500.00"));

        assertThat(dto.productId()).isEqualTo(10L);
        assertThat(dto.quantity()).isEqualTo(2);
        assertThat(dto.unitPrice()).isEqualTo(new BigDecimal("8500.00"));
    }

    @Test
    void shouldCreateOrderItemRequestDtoWithoutUnitPrice() {
        OrderItemRequestDto dto = new OrderItemRequestDto(10L, 2, null);

        assertThat(dto.productId()).isEqualTo(10L);
        assertThat(dto.quantity()).isEqualTo(2);
        assertThat(dto.unitPrice()).isNull();
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        OrderItemRequestDto dto1 = new OrderItemRequestDto(10L, 2, new BigDecimal("100"));
        OrderItemRequestDto dto2 = new OrderItemRequestDto(10L, 2, new BigDecimal("100"));
        OrderItemRequestDto dto3 = new OrderItemRequestDto(20L, 1, new BigDecimal("200"));

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        OrderItemRequestDto dto = new OrderItemRequestDto(10L, 2, new BigDecimal("100"));
        String toString = dto.toString();

        assertThat(toString).contains("productId=10");
        assertThat(toString).contains("quantity=2");
    }
}