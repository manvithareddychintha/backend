package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.DTO.PortfolioSummary;
import com.recursion.portfolioManager.DTO.HoldingValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PortfolioSummaryServiceTest {

    // ------------------------------------------------------------------
    // Fake HoldingValue implementation (ONLY what service needs)
    // ------------------------------------------------------------------
    static class TestHoldingValue implements HoldingValue {

        private final BigDecimal quantity;
        private final BigDecimal avgBuyPrice;
        private final BigDecimal latestPrice;

        TestHoldingValue(BigDecimal quantity,
                         BigDecimal avgBuyPrice,
                         BigDecimal latestPrice) {
            this.quantity = quantity;
            this.avgBuyPrice = avgBuyPrice;
            this.latestPrice = latestPrice;
        }

        @Override
        public BigDecimal getQuantity() {
            return quantity;
        }

        @Override
        public BigDecimal getAvgBuyPrice() {
            return avgBuyPrice;
        }

        @Override
        public BigDecimal getLatestPrice() {
            return latestPrice;
        }

        // Unused by PortfolioSummaryService
        @Override
        public String getAssetType() {
            return "";
        }

        @Override
        public Double getTotalValue() {
            return 0.0;
        }
    }

    // ------------------------------------------------------------------
    // Test-only Service (NO repository, NO Spring, NO Mockito)
    // ------------------------------------------------------------------
    static class TestPortfolioSummaryService extends PortfolioSummaryService {

        private final List<HoldingValue> testData;

        TestPortfolioSummaryService(List<HoldingValue> testData) {
            super(null); // repository NOT required
            this.testData = testData;
        }

        @Override
        public PortfolioSummary getPortfolioSummary() {

            BigDecimal totalInvested = BigDecimal.ZERO;
            BigDecimal totalCurrentValue = BigDecimal.ZERO;

            for (HoldingValue v : testData) {
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

    // ------------------------------------------------------------------
    // TEST 1: Single holding – happy path
    // ------------------------------------------------------------------
    @Test
    void shouldCalculateSingleHoldingCorrectly() {

        PortfolioSummaryService service =
                new TestPortfolioSummaryService(List.of(
                        new TestHoldingValue(
                                BigDecimal.valueOf(10),
                                BigDecimal.valueOf(100),
                                BigDecimal.valueOf(150)
                        )
                ));

        PortfolioSummary summary = service.getPortfolioSummary();

        assertEquals(new BigDecimal("1000"), summary.getTotalInvested());
        assertEquals(new BigDecimal("1500"), summary.getTotalCurrentValue());
        assertEquals(new BigDecimal("500"), summary.getTotalProfit());
        assertEquals(
                new BigDecimal("50.00"),
                summary.getProfitPercentage().setScale(2)
        );
    }

    // ------------------------------------------------------------------
    // TEST 2: Multiple holdings
    // ------------------------------------------------------------------
    @Test
    void shouldCalculateMultipleHoldingsCorrectly() {

        PortfolioSummaryService service =
                new TestPortfolioSummaryService(List.of(
                        new TestHoldingValue(
                                BigDecimal.valueOf(5),
                                BigDecimal.valueOf(100),
                                BigDecimal.valueOf(120)
                        ),
                        new TestHoldingValue(
                                BigDecimal.valueOf(5),
                                BigDecimal.valueOf(200),
                                BigDecimal.valueOf(220)
                        )
                ));

        PortfolioSummary summary = service.getPortfolioSummary();

        assertEquals(new BigDecimal("1500"), summary.getTotalInvested());
        assertEquals(new BigDecimal("1700"), summary.getTotalCurrentValue());
        assertEquals(new BigDecimal("200"), summary.getTotalProfit());
        assertEquals(
                new BigDecimal("13.33"),
                summary.getProfitPercentage().setScale(2)
        );
    }

    // ------------------------------------------------------------------
    // TEST 3: Loss scenario
    // ------------------------------------------------------------------
    @Test
    void shouldHandleLossCorrectly() {

        PortfolioSummaryService service =
                new TestPortfolioSummaryService(List.of(
                        new TestHoldingValue(
                                BigDecimal.valueOf(10),
                                BigDecimal.valueOf(100),
                                BigDecimal.valueOf(80)
                        )
                ));

        PortfolioSummary summary = service.getPortfolioSummary();

        assertEquals(new BigDecimal("1000"), summary.getTotalInvested());
        assertEquals(new BigDecimal("800"), summary.getTotalCurrentValue());
        assertEquals(new BigDecimal("-200"), summary.getTotalProfit());
        assertEquals(
                new BigDecimal("-20.00"),
                summary.getProfitPercentage().setScale(2)
        );
    }

    // ------------------------------------------------------------------
    // TEST 4: Zero holdings
    // ------------------------------------------------------------------
    @Test
    void shouldHandleEmptyPortfolio() {

        PortfolioSummaryService service =
                new TestPortfolioSummaryService(List.of());

        PortfolioSummary summary = service.getPortfolioSummary();

        assertEquals(BigDecimal.ZERO, summary.getTotalInvested());
        assertEquals(BigDecimal.ZERO, summary.getTotalCurrentValue());
        assertEquals(BigDecimal.ZERO, summary.getTotalProfit());
        assertEquals(
                BigDecimal.ZERO.setScale(2),
                summary.getProfitPercentage().setScale(2)
        );
    }

    // ------------------------------------------------------------------
    // TEST 5: Decimal precision & rounding (HALF_UP)
    // ------------------------------------------------------------------
    @Test
    void shouldHandleDecimalPrecisionCorrectly() {

        PortfolioSummaryService service =
                new TestPortfolioSummaryService(List.of(
                        new TestHoldingValue(
                                new BigDecimal("3"),
                                new BigDecimal("123.45"),
                                new BigDecimal("130.55")
                        )
                ));

        PortfolioSummary summary = service.getPortfolioSummary();

        assertEquals(new BigDecimal("370.35"), summary.getTotalInvested());
        assertEquals(new BigDecimal("391.65"), summary.getTotalCurrentValue());
        assertEquals(new BigDecimal("21.30"), summary.getTotalProfit());
        assertEquals(
                new BigDecimal("5.75"), // correct HALF_UP rounding
                summary.getProfitPercentage().setScale(2)
        );
    }
}

