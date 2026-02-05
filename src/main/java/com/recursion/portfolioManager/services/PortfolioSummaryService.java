package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.DTO.InvestedValueDTO;
import com.recursion.portfolioManager.DTO.PortfolioSummary;
import com.recursion.portfolioManager.DTO.HoldingValue;
import com.recursion.portfolioManager.DTO.PortfolioValueDTO;
import com.recursion.portfolioManager.models.Holdings;
import com.recursion.portfolioManager.models.StockPrice30Days;
import com.recursion.portfolioManager.repositories.HoldingsRepository;
import com.recursion.portfolioManager.repositories.StockPrice30daysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioSummaryService {

    @Autowired
    private final HoldingsRepository holdingsRepository;

    @Autowired
    public StockPrice30daysRepository stockPrice30daysRepository;

    public PortfolioSummaryService(HoldingsRepository holdingsRepository) {
        this.holdingsRepository = holdingsRepository;
    }

    public List<InvestedValueDTO> getInvestedValue()
    {
        return holdingsRepository.findTotalInvestedValueBySymbol();
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

    public List<PortfolioValueDTO> getHoldingsTotalValue() {
        List<PortfolioValueDTO> portfolioValues = new ArrayList<>();

        List<Holdings> holdings = holdingsRepository.findAll();

        Map<LocalDate,BigDecimal> map = new HashMap<>();

        for (Holdings holding : holdings) {
            String symbol = holding.getSymbol();
            BigDecimal quantity = holding.getQuantity();
            BigDecimal avgBuyPrice = holding.getAvgBuyPrice();
            LocalDateTime dateAdded = holding.getCreatedAt();

            LocalDate dateAddedLocalDate = dateAdded.toLocalDate();

            List<StockPrice30Days> stockPrices = stockPrice30daysRepository.findBySymbolAndLocalDateAfter(symbol, dateAddedLocalDate);

            for (StockPrice30Days price : stockPrices) {
                BigDecimal dailyValue = price.getClosePrice().multiply(quantity);
                if(!map.containsKey(price.getLocalDate()))
                map.put(price.getLocalDate(), dailyValue);
                else map.put(price.getLocalDate(),map.get(price.getLocalDate()).add(dailyValue));
            }
        }

        for(Map.Entry<LocalDate, BigDecimal> entry:map.entrySet())
        {
            portfolioValues.add(new PortfolioValueDTO(entry.getKey(),entry.getValue()));
        }
        return portfolioValues;
    }
}
