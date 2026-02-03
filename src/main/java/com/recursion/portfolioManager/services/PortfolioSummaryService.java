package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.DTO.InvestedValueDTO;
import com.recursion.portfolioManager.DTO.PortfolioSummary;
import com.recursion.portfolioManager.repositories.HoldingValue;
import com.recursion.portfolioManager.repositories.HoldingsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PortfolioSummaryService {

    private final HoldingsRepository holdingsRepository;

    public PortfolioSummaryService(HoldingsRepository holdingsRepository) {
        this.holdingsRepository = holdingsRepository;
    }

    public List<InvestedValueDTO> getInvestedValue()
    {
        return holdingsRepository.findInvestedValueBySymbol();
    }


    public PortfolioSummary getPortfolioSummary() {

        List<HoldingValue> values = holdingsRepository.findHoldingValues();

        BigDecimal totalInvested = BigDecimal.ZERO;
        BigDecimal totalCurrentValue = BigDecimal.ZERO;

        for (HoldingValue v : values) {
            totalInvested = totalInvested.add(
                    v.getAvgBuyPrice().multiply(v.getQuantity())
            );

            totalCurrentValue = totalCurrentValue.add(
                    v.getLatestPrice().multiply(v.getQuantity())
            );
        }

        BigDecimal totalProfit = totalCurrentValue.subtract(totalInvested);

        BigDecimal profitPercentage = BigDecimal.ZERO;
        if (totalInvested.compareTo(BigDecimal.ZERO) > 0) {
            profitPercentage = totalProfit
                    .multiply(BigDecimal.valueOf(100))
                    .divide(totalInvested, 2, RoundingMode.HALF_UP);
        }

        return new PortfolioSummary(
                totalInvested,
                totalCurrentValue,
                totalProfit,
                profitPercentage
        );
    }
}
