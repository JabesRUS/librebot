package com.jabes.librebot.model.dto.libreview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для получения показаний из Data в LibreView API.
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GlucoseMeasurementDto {

    /**
     * Время показания
     */
    @JsonProperty("Timestamp")
    private String timeStamp;

    /**
     * Показание значения в mg/dL
     */
    @JsonProperty("ValueInMgPerDl")
    private Integer valueInMgPerDl;

    /**
     * Показание значения в mmol/L
     */
    @JsonProperty("Value")
    private Double valueInMmol;

    /**
     *  Стрелка тренда
     */
    @JsonProperty("TrendArrow")
    private Integer trendArrow;


}
