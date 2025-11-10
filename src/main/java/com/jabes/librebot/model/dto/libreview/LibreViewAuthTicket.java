package com.jabes.librebot.model.dto.libreview;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO для информации о JWT токене авторизации LibreView API.
 * Токен используется для всех последующих запросов к API.
 */
@Getter
@Setter
public class LibreViewAuthTicket {

    /**
     * JWT токен для авторизации последующих запросов к LibreView API
     */
    private String token;

    /**
     * Время истечения токена (Unix timestamp в секундах).
     * Используется Long вместо long, чтобы поле могло быть null.
     */
    private Long expires;

    /**
     * Длительность действия токена в миллисекундах.
     * Используется Long вместо long, чтобы поле могло быть null.
     */
    private Long duration;
}
