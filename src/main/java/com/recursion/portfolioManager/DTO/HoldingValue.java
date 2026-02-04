package com.recursion.portfolioManager.DTO;

import java.math.BigDecimal;

public interface HoldingValue {

    BigDecimal getQuantity();
    BigDecimal getAvgBuyPrice();
    BigDecimal getLatestPrice();

    String getAssetType();
    Double getTotalValue();

}
