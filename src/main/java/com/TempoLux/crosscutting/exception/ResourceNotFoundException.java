package com.TempoLux.crosscutting.exception;

/**
 * Recurso solicitado no encontrado (producto, pedido, usuario...).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
