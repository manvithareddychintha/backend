package com.recursion.portfolioManager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class StockPrice30Days {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private LocalDate localDate;
    private BigDecimal closePrice;
}
