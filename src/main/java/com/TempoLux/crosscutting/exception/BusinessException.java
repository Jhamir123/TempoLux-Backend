package com.TempoLux.crosscutting.exception;

/**
 * Excepción base de negocio de TempoLux.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
