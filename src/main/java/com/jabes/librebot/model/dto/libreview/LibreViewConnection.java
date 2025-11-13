package com.jabes.librebot.model.dto.libreview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для одного подключения (пациента) из LibreView API.
 * Содержит информацию о пациенте и его последнее измерение глюкозы.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class LibreViewConnection {

    /**
     * Id пациента.
     */
    @JsonProperty("patientId")
    private String patientId;

    /**
     * Индекс страны.
     */
    @JsonProperty("country")
    private String country;

    /**
     * Имя
     */
    @JsonProperty("firstName")
    private String firstName;

    /**
     * Фамилия
     */
    @JsonProperty("lastName")
    private String lastName;

    /**
     *  единицы измерения (0=mg/dL, 1=mmol/L)
     */
    @JsonProperty("uom")
    private Integer uom;

    /**
     * Последнее измерение
     */
    @JsonProperty("glucoseMeasurement")
    private GlucoseMeasurementDto glucoseMeasurement;
}
