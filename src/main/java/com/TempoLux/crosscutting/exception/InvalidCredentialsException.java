package com.TempoLux.crosscutting.exception;

/**
 * Credenciales inválidas durante el inicio de sesión.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
