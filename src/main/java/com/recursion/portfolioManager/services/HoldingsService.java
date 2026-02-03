package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.DTO.HoldingRequest;
import com.recursion.portfolioManager.DTO.HoldingWithPriceDTO;
import com.recursion.portfolioManager.models.Holdings;
import com.recursion.portfolioManager.models.other.AssetType;
import com.recursion.portfolioManager.repositories.HoldingsRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HoldingsService {
    @Autowired
    private HoldingsRepository holdingRepository;

    @Autowired
    MarketApiService marketApiService;

    public Holdings saveHolding(HoldingRequest request) {

        boolean valid = marketApiService.isValidSymbol(
                request.getSymbol(),
                request.getAssetType()
        );
//        System.out.println(valid);

        if (!valid) {
            throw new IllegalArgumentException("Invalid company / asset symbol");
        }

        if ("CASH".equalsIgnoreCase(request.getSymbol())) {
            Optional<Holdings> existingCashHolding = holdingRepository.findBySymbolIgnoreCase(request.getSymbol());

            if (existingCashHolding.isPresent()) {
                Holdings cashHolding = existingCashHolding.get();
                cashHolding.setQuantity(cashHolding.getQuantity().add(request.getQuantity()));
                cashHolding.setAvgBuyPrice(request.getAvgBuyPrice());
                return holdingRepository.save(cashHolding);
            } else {
                // If no "CASH" holding exists, create a new one
                Holdings newCashHolding = new Holdings();
                newCashHolding.setAssetType(request.getAssetType());
                newCashHolding.setSymbol(request.getSymbol());
                newCashHolding.setAssetName(request.getAssetName());
                newCashHolding.setQuantity(request.getQuantity());
                newCashHolding.setAvgBuyPrice(request.getAvgBuyPrice());
                newCashHolding.setCreatedAt(LocalDateTime.now());
                return holdingRepository.save(newCashHolding);
            }
        }
        Holdings holding = new Holdings();
        holding.setAssetType(request.getAssetType());
        holding.setSymbol(request.getSymbol());
        holding.setAssetName(request.getAssetName());
        holding.setQuantity(request.getQuantity());
        holding.setAvgBuyPrice(request.getAvgBuyPrice());
        holding.setCreatedAt(LocalDateTime.now());
        return holdingRepository.save(holding);
    }

    public List<Holdings> getAll()
    {
        return holdingRepository.findAll();
    }

    public Holdings update(Long id,HoldingRequest request)
    {

        if(holdingRepository.existsById(id))
            holdingRepository.deleteById(id);
        Holdings holding = new Holdings();
        holding.setAssetType(request.getAssetType());
        holding.setSymbol(request.getSymbol());
        holding.setAssetName(request.getAssetName());
        holding.setQuantity(request.getQuantity());
        holding.setAvgBuyPrice(request.getAvgBuyPrice());
        holding.setCreatedAt(LocalDateTime.now());
        ;return holdingRepository.save(holding);
    }

    public boolean deleteById(Long id){
        boolean re=holdingRepository.existsById(id);
        if(re)
            holdingRepository.deleteById(id);
        return re;
    }

    public Optional<Holdings> fetchById(Long id)
    {
//        if(holdingRepository.existsById(id))
            return holdingRepository.findById(id);
//        return ResponseEntity.noContent().build();

    }
    public List<HoldingWithPriceDTO> findHoldingsWithLatestPriceAndProfit()
    {
        return holdingRepository.findHoldingsWithLatestPriceAndProfit();
    }
}
