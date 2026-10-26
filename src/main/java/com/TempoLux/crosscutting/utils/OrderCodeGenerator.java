package com.TempoLux.crosscutting.utils;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Genera el código único de una orden con el formato del enunciado: ORD-AAAA-XXXX
 * (ej. "ORD-2026-99A").
 */
public final class OrderCodeGenerator {

    private static final String SUFFIX_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";

    private OrderCodeGenerator() {
    }

    public static String generate() {
        int year = Year.now().getValue();
        return String.format("ORD-%d-%s", year, randomSuffix(3));
    }

    private static String randomSuffix(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(SUFFIX_ALPHABET.charAt(random.nextInt(SUFFIX_ALPHABET.length())));
        }
        return sb.toString();
    }
}
