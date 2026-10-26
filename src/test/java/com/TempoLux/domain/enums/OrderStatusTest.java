package com.TempoLux.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        assertThat(OrderStatus.values()).containsExactly(
                OrderStatus.PENDIENTE,
                OrderStatus.EN_PREPARACION,
                OrderStatus.EN_TRANSITO,
                OrderStatus.ENTREGADO
        );
    }

    @Test
    void shouldGetCorrectNames() {
        assertThat(OrderStatus.PENDIENTE.name()).isEqualTo("PENDIENTE");
        assertThat(OrderStatus.EN_PREPARACION.name()).isEqualTo("EN_PREPARACION");
        assertThat(OrderStatus.EN_TRANSITO.name()).isEqualTo("EN_TRANSITO");
        assertThat(OrderStatus.ENTREGADO.name()).isEqualTo("ENTREGADO");
    }
}