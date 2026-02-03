package com.recursion.portfolioManager.DTO;

import java.math.BigDecimal;

public class PortfolioSummary {

    private BigDecimal totalInvested;
    private BigDecimal totalCurrentValue;
    private BigDecimal totalProfit;
    private BigDecimal profitPercentage;

    public PortfolioSummary(BigDecimal totalInvested,
                            BigDecimal totalCurrentValue,
                            BigDecimal totalProfit,
                            BigDecimal profitPercentage) {
        this.totalInvested = totalInvested;
        this.totalCurrentValue = totalCurrentValue;
        this.totalProfit = totalProfit;
        this.profitPercentage = profitPercentage;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public BigDecimal getTotalCurrentValue() {
        return totalCurrentValue;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public BigDecimal getProfitPercentage() {
        return profitPercentage;
    }
}
