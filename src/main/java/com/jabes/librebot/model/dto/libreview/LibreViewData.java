package com.jabes.librebot.model.dto.libreview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для вложенного объекта "data" из ответа LibreView API.
 * Содержит информацию о токене авторизации и пользователе.
 */
@Getter @Setter
public class LibreViewData {

    /**
     * Информация о токене авторизации
     */
    @JsonProperty("authTicket")
    private LibreViewAuthTicket authTicket;

    /**
     * Информация о пользователе
     */
    private LibreViewUser user;
}
