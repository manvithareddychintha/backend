package com.recursion.portfolioManager.repositories;

import java.math.BigDecimal;

public interface HoldingValue {

    BigDecimal getQuantity();
    BigDecimal getAvgBuyPrice();
    BigDecimal getLatestPrice();
}
