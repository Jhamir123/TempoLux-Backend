package com.TempoLux.crosscutting.exception;

/**
 * Recurso duplicado (por ejemplo, el correo ya registrado).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
