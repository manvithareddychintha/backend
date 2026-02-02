package com.recursion.portfolioManager.controllers;

import com.recursion.portfolioManager.DTO.HoldingRequest;
import com.recursion.portfolioManager.models.Holdings;
import com.recursion.portfolioManager.services.HoldingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/holdings")
public class HoldingController {

    @Autowired
    private HoldingsService holdingService;

    @PostMapping
    public ResponseEntity addHolding(@RequestBody HoldingRequest request) {
        try {
            Holdings saved = holdingService.saveHolding(request);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
