package com.recursion.portfolioManager.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class InvestedValueDTO {

    private String symbol;
    private BigDecimal investedValue;
}
