package com.jabes.librebot.model.dto.libreview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Класс для корневого объекта
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class LibreViewConnectionsResponse {

    /**
     * Код статуса
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * Массив подключений. В подключении котором текущее показание датчика
     */
    @JsonProperty("data")
    private List<LibreViewConnection> data;

    /**
     * Обновленные данные токена.
     */
    @JsonProperty("ticket")
    private LibreViewAuthTicket ticket;


}
