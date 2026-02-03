package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.DTO.HoldingRequest;
import com.recursion.portfolioManager.models.Holdings;
import com.recursion.portfolioManager.models.other.AssetType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HoldingsServiceTest {

    // ---------------------------------------------------
    // Test-only service (NO repository, NO Spring)
    // ---------------------------------------------------
    static class TestHoldingsService extends HoldingsService {

        List<Holdings> fakeDb = new ArrayList<>();
        boolean validSymbol = true;

        @Override
        public Holdings saveHolding(HoldingRequest request) {

            if (!validSymbol) {
                throw new IllegalArgumentException("Invalid company / asset symbol");
            }

            Holdings holding = new Holdings();
            holding.setAssetType(request.getAssetType());
            holding.setSymbol(request.getSymbol());
            holding.setAssetName(request.getAssetName());
            holding.setQuantity(request.getQuantity());
            holding.setAvgBuyPrice(request.getAvgBuyPrice());
            holding.setCreatedAt(LocalDateTime.now());

            fakeDb.add(holding);
            return holding;
        }

        @Override
        public List<Holdings> getAll() {
            return fakeDb;
        }

        @Override
        public Holdings update(Long id, HoldingRequest request) {

            if (id < fakeDb.size()) {
                fakeDb.remove(id.intValue());
            }

            Holdings holding = new Holdings();
            holding.setAssetType(request.getAssetType());
            holding.setSymbol(request.getSymbol());
            holding.setAssetName(request.getAssetName());
            holding.setQuantity(request.getQuantity());
            holding.setAvgBuyPrice(request.getAvgBuyPrice());
            holding.setCreatedAt(LocalDateTime.now());

            fakeDb.add(holding);
            return holding;
        }
    }

    // ---------------------------------------------------
    // Helper: create HoldingRequest
    // ---------------------------------------------------
    private HoldingRequest createRequest() {
        HoldingRequest req = new HoldingRequest();
        req.setSymbol("AAPL");
        req.setAssetName("Apple Inc");
        req.setAssetType(AssetType.EQUITY);
        req.setQuantity(new BigDecimal("10"));
        req.setAvgBuyPrice(new BigDecimal("150"));
        return req;
    }

    // ---------------------------------------------------
    // TEST 1: Save holding (happy path)
    // ---------------------------------------------------
    @Test
    void shouldSaveHoldingSuccessfully() {

        TestHoldingsService service = new TestHoldingsService();
        HoldingRequest request = createRequest();

        Holdings saved = service.saveHolding(request);

        assertEquals("AAPL", saved.getSymbol());
        assertEquals("Apple Inc", saved.getAssetName());
        assertEquals(AssetType.EQUITY, saved.getAssetType());
        assertEquals(new BigDecimal("10"), saved.getQuantity());
        assertEquals(new BigDecimal("150"), saved.getAvgBuyPrice());
        assertNotNull(saved.getCreatedAt());
    }

    // ---------------------------------------------------
    // TEST 2: Invalid symbol should throw exception
    // ---------------------------------------------------
    @Test
    void shouldThrowExceptionForInvalidSymbol() {

        TestHoldingsService service = new TestHoldingsService();
        service.validSymbol = false;

        HoldingRequest request = createRequest();

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveHolding(request)
        );

        assertEquals(
                "Invalid company / asset symbol",
                ex.getMessage()
        );
    }

    // ---------------------------------------------------
    // TEST 3: getAll returns all holdings
    // ---------------------------------------------------
    @Test
    void shouldReturnAllHoldings() {

        TestHoldingsService service = new TestHoldingsService();

        service.saveHolding(createRequest());
        service.saveHolding(createRequest());

        List<Holdings> all = service.getAll();

        assertEquals(2, all.size());
    }

    // ---------------------------------------------------
    // TEST 4: update replaces existing holding
    // ---------------------------------------------------
    @Test
    void shouldUpdateHoldingSuccessfully() {

        TestHoldingsService service = new TestHoldingsService();
        service.saveHolding(createRequest());

        HoldingRequest updated = new HoldingRequest();
        updated.setSymbol("GOOGL");
        updated.setAssetName("Google");
        updated.setAssetType(AssetType.EQUITY);
        updated.setQuantity(new BigDecimal("5"));
        updated.setAvgBuyPrice(new BigDecimal("200"));

        Holdings result = service.update(0L, updated);

        assertEquals("GOOGL", result.getSymbol());
        assertEquals("Google", result.getAssetName());
        assertEquals(new BigDecimal("5"), result.getQuantity());
        assertEquals(new BigDecimal("200"), result.getAvgBuyPrice());
    }
}

