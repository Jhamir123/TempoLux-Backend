package com.TempoLux.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCategoryTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        assertThat(ProductCategory.values()).containsExactly(
                ProductCategory.ALTA_RELOJERIA,
                ProductCategory.DEPORTIVO,
                ProductCategory.ARTESANAL_3D
        );
    }

    @Test
    void shouldGetCorrectNames() {
        assertThat(ProductCategory.ALTA_RELOJERIA.name()).isEqualTo("ALTA_RELOJERIA");
        assertThat(ProductCategory.DEPORTIVO.name()).isEqualTo("DEPORTIVO");
        assertThat(ProductCategory.ARTESANAL_3D.name()).isEqualTo("ARTESANAL_3D");
    }
}