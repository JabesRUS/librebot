package com.jabes.librebot.exception;

public class LibreViewApiException extends RuntimeException {
    public LibreViewApiException(String message) {
        super(message);
    }

    public LibreViewApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
