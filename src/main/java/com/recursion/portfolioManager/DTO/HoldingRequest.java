package com.recursion.portfolioManager.DTO;

import com.recursion.portfolioManager.models.other.AssetType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldingRequest {
    private AssetType assetType;
    private String symbol;
    private String assetName;
    private BigDecimal quantity;
    private BigDecimal avgBuyPrice;
}
