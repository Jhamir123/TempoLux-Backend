package com.TempoLux.domain.dto.response;

/**
 * Opción de color/esfera de un reloj, alineado con la estructura
 * que consume el frontend (colors): id, name, hex, dialHex, accentGlow.
 */
public record ProductColorOption(String id, String name, String hex, String dialHex, String accentGlow) {
}
