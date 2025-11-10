package com.jabes.librebot.model.dto.libreview;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO для информации о пользователе LibreView.
 * Содержит базовые данные пользователя для персонализации бота.
 */
@Getter @Setter
public class LibreViewUser {

    /**
     * Уникальный идентификатор пользователя в системе LibreView
     */
    private String id;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Email пользователя
     */
    private String email;

    /**
     * Код страны пользователя (например, "RU")
     */
    private String country;
}
