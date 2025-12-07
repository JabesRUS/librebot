package com.jabes.librebot.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtils {

    /**
     * Преобразует строку с форматом "MM/dd/yyyy hh:mm:ss a"
     * в объект LocalDateTime.
     *
     * @param value строковое представление даты
     * @return LocalDateTime
     */
    public LocalDateTime parseUsDate(String value) {

        // Создаем форматтер под американский формат
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a");

        // Парсим строку с учетом AM/PM
        return LocalDateTime.parse(value, formatter);
    }

}
