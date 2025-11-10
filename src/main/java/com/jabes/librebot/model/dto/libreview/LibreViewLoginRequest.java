package com.jabes.librebot.model.dto.libreview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO для запроса авторизации в LibreView API.
 * Отправляется на эндпоинт POST /llu/auth/login
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@ToString(exclude = "password")
public class LibreViewLoginRequest {
    /**
     * Email пользователя LibreView
     */
    private String email;

    /**
     * Пароль пользователя LibreView
     */
    private String password;
}
