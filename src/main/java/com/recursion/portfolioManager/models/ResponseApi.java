package com.recursion.portfolioManager.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ResponseApi {

    @JsonProperty("Meta Data")
    private MetaData metaData;
    @JsonProperty("Time Series (Daily)")
    private Map<String, TimeSeriesPerDay> timeSeriesDaily ;
}
