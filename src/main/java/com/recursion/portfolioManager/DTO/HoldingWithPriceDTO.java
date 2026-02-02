package com.recursion.portfolioManager.DTO;

import com.recursion.portfolioManager.models.other.AssetType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldingWithPriceDTO {

    private Long id;
    private String symbol;
    private String assetName;
    private AssetType assetType;
    private BigDecimal quantity;
    private BigDecimal avgBuyPrice;
    private BigDecimal currentPrice;
    private BigDecimal profit;

    public HoldingWithPriceDTO(Long id, String symbol, String assetName,
                               AssetType assetType, BigDecimal quantity,
                               BigDecimal avgBuyPrice, BigDecimal currentPrice,
                               BigDecimal profit) {
        this.id = id;
        this.symbol = symbol;
        this.assetName = assetName;
        this.assetType = assetType;
        this.quantity = quantity;
        this.avgBuyPrice = avgBuyPrice;
        this.currentPrice = currentPrice;
        this.profit = profit;
    }
}

