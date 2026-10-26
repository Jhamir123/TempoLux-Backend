package com.TempoLux.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatToolNameTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        assertThat(ChatToolName.values()).containsExactly(
                ChatToolName.SEARCH_PRODUCTS,
                ChatToolName.GET_PRODUCT_DETAILS,
                ChatToolName.GET_ORDER_STATUS
        );
    }

    @Test
    void shouldGetCorrectNames() {
        assertThat(ChatToolName.SEARCH_PRODUCTS.name()).isEqualTo("SEARCH_PRODUCTS");
        assertThat(ChatToolName.GET_PRODUCT_DETAILS.name()).isEqualTo("GET_PRODUCT_DETAILS");
        assertThat(ChatToolName.GET_ORDER_STATUS.name()).isEqualTo("GET_ORDER_STATUS");
    }
}