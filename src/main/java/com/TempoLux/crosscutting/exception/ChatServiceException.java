package com.TempoLux.crosscutting.exception;

/**
 * Error de comunicación o procesamiento con la API de Groq / LuxBot.
 */
public class ChatServiceException extends RuntimeException {

    public ChatServiceException(String message) {
        super(message);
    }

    public ChatServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
