package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.DTO.HoldingRequest;
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

        if (!valid) {
            throw new IllegalArgumentException("Invalid company / asset symbol");
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

    public void deleteById(Long id){
        holdingRepository.deleteById(id);
    }

    public ResponseEntity<Optional<Holdings>> fetchById(Long id)
    {
        if(holdingRepository.existsById(id))
            return ResponseEntity.ok(holdingRepository.findById(id));
        return ResponseEntity.noContent().build();

    }
}
