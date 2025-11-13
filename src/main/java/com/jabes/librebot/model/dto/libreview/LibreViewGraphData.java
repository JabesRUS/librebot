package com.jabes.librebot.model.dto.libreview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 *
 */
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class LibreViewGraphData {

    /**
     *
     */
    @JsonProperty("graphData")
    private List<GlucoseMeasurementDto> glucoseMeasurements;
}
