package com.recursion.portfolioManager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class AssetPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;              // BTC, ETH, AAPL
    private BigDecimal price;           // current market price
    private LocalDateTime timestamp;    // when this price was fetched
}
