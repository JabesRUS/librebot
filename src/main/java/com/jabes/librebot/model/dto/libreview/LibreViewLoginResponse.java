package com.jabes.librebot.model.dto.libreview;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO для ответа от LibreView API при авторизации.
 * Получается с эндпоинта POST /llu/auth/login
 */
@Getter @Setter
public class LibreViewLoginResponse {

    /**
     * Статус ответа. 0 = успех, другое значение = ошибка
     */
    private Integer status;

    /**
     * Данные ответа (токен, информация о пользователе и т.д.)
     */
    private LibreViewData data;
}
