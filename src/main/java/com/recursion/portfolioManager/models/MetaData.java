package com.recursion.portfolioManager.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MetaData {

    @JsonProperty("1. Information")
    private String information;
    @JsonProperty("2. Symbol")
    private String symbol;
    @JsonProperty("3. Last Refreshed")
    private String last_refreshed;
    @JsonProperty("4. Output Size")
    private String ouput_size;
    @JsonProperty("5. Time Zone")
    private String time_zone;

}
