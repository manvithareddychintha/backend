package com.recursion.portfolioManager.models;

import com.recursion.portfolioManager.models.other.AssetType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Holdings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AssetType assetType;

    private String symbol;
    private String assetName;

    private BigDecimal quantity;
    private BigDecimal avgBuyPrice;

    private LocalDateTime createdAt;
}