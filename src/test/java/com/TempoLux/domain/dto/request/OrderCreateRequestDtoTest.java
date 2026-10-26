package com.TempoLux.domain.dto.request;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderCreateRequestDtoTest {

    @Test
    void shouldCreateOrderCreateRequestDtoWithAllFields() {
        List<OrderItemRequestDto> items = List.of(
                new OrderItemRequestDto(10L, 2, new BigDecimal("8500.00")),
                new OrderItemRequestDto(20L, 1, new BigDecimal("5200.00"))
        );

        OrderCreateRequestDto dto = new OrderCreateRequestDto(
                "Juan Pérez",
                "juan@test.com",
                items
        );

        assertThat(dto.customerName()).isEqualTo("Juan Pérez");
        assertThat(dto.customerEmail()).isEqualTo("juan@test.com");
        assertThat(dto.items()).hasSize(2);
        assertThat(dto.items().get(0).productId()).isEqualTo(10L);
        assertThat(dto.items().get(0).quantity()).isEqualTo(2);
        assertThat(dto.items().get(1).productId()).isEqualTo(20L);
        assertThat(dto.items().get(1).quantity()).isEqualTo(1);
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        List<OrderItemRequestDto> items = List.of(new OrderItemRequestDto(10L, 1, new BigDecimal("100")));
        OrderCreateRequestDto dto1 = new OrderCreateRequestDto("Juan", "juan@test.com", items);
        OrderCreateRequestDto dto2 = new OrderCreateRequestDto("Juan", "juan@test.com", items);
        OrderCreateRequestDto dto3 = new OrderCreateRequestDto("Pedro", "pedro@test.com", items);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
    }

    @Test
    void shouldSupportToString() {
        OrderCreateRequestDto dto = new OrderCreateRequestDto("Test", "test@test.com", List.of());
        String toString = dto.toString();

        assertThat(toString).contains("customerName=Test");
        assertThat(toString).contains("customerEmail=test@test.com");
    }
}