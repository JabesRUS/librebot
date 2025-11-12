package com.jabes.librebot.exception;


/**
 * Исключение, возникающее при ошибках авторизации в LibreView API.
 *
 * Используется для:
 * - Неверные credentials (401)
 * - Недоступность API (timeout, 5xx)
 * - Некорректный формат ответа
 */
public class LibreViewAuthException extends RuntimeException {
    public LibreViewAuthException(String message) {
        super(message);
    }

    public LibreViewAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
