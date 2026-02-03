package com.recursion.portfolioManager.controllers;

import com.recursion.portfolioManager.DTO.HoldingRequest;
import com.recursion.portfolioManager.DTO.HoldingWithPriceDTO;
import com.recursion.portfolioManager.models.Holdings;
import com.recursion.portfolioManager.repositories.HoldingsRepository;
import com.recursion.portfolioManager.services.HoldingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/holdings")
public class HoldingController {

    @Autowired
    private HoldingsService holdingService;
    @Autowired
    private HoldingsRepository holdingsRepository;

    @PostMapping
    public ResponseEntity addHolding(@RequestBody HoldingRequest request) {
        try {
            Holdings saved = holdingService.saveHolding(request);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @GetMapping
    public ResponseEntity getHoldings(){
        List<Holdings> holdingsList=holdingService.getAll();
        return ResponseEntity.ok(holdingsList);
    }

    @GetMapping("/latest")
    public List<HoldingWithPriceDTO> getHoldingsWithLatestprice() {
        return holdingsRepository.findHoldingsWithLatestPriceAndProfit();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateStocks(@PathVariable Long id,@RequestBody HoldingRequest holdings)
    {
        holdingService.update(id,holdings);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStocks(@PathVariable Long id)
    {
        holdingService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity fetchById(@PathVariable Long id)
    {
        return holdingService.fetchById(id);
    }

}
