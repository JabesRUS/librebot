package com.jabes.librebot.model.dto.libreview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 */
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
public class LibreViewGraphResponse {

    /**
     *
     */
    @JsonProperty("status")
    private Integer status;

    /**
     *
     */
    @JsonProperty("data")
    private LibreViewGraphData graphData;

    /**
     *
     */
    @JsonProperty("ticket")
    private LibreViewAuthTicket ticket;
}
