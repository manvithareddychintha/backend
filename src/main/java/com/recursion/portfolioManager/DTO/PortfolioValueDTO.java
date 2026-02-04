package com.recursion.portfolioManager.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PortfolioValueDTO {

    private LocalDate date;
    private BigDecimal totalValue;

    public PortfolioValueDTO(LocalDate date, BigDecimal totalValue) {
        this.date = date;
        this.totalValue = totalValue;
    }

}

