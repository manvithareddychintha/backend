package com.recursion.portfolioManager.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.recursion.portfolioManager.models.other.LocalDataKeyDeserializer;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ResponseApi {

    @JsonProperty("Meta Data")
    private MetaData metaData;
    @JsonProperty("Time Series (Daily)")
    @JsonDeserialize(keyUsing = LocalDataKeyDeserializer.class)
    private Map<LocalDate, TimeSeriesPerDay> timeSeriesDaily ;
}
