package com.TempoLux.crosscutting.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderCodeGeneratorTest {

    @Test
    void shouldGenerateOrderCodeWithCorrectFormat() {
        String code = OrderCodeGenerator.generate();

        assertThat(code).matches("ORD-\\d{4}-[A-Z0-9]{3}");
        assertThat(code).startsWith("ORD-" + java.time.Year.now().getValue() + "-");
    }

    @Test
    void shouldGenerateUniqueCodes() {
        String code1 = OrderCodeGenerator.generate();
        String code2 = OrderCodeGenerator.generate();

        // Codes should be different (very high probability)
        assertThat(code1).isNotEqualTo(code2);
    }

    @Test
    void shouldGenerateCodeWithValidSuffixCharacters() {
        String code = OrderCodeGenerator.generate();
        String suffix = code.substring(code.lastIndexOf('-') + 1);

        // Suffix should only contain valid characters (no I, O, Q, etc.)
        assertThat(suffix).matches("[A-HJ-NPR-Z0-9]{3}");
    }

    @Test
    void shouldGenerateMultipleCodesWithDifferentSuffixes() {
        var codes = new java.util.HashSet<String>();
        for (int i = 0; i < 100; i++) {
            codes.add(OrderCodeGenerator.generate());
        }
        // All should be unique (very high probability with 33^3 = 35,937 combinations)
        assertThat(codes).hasSize(100);
    }
}