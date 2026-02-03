package com.recursion.portfolioManager.controllers;

import com.recursion.portfolioManager.DTO.PortfolioSummary;
import com.recursion.portfolioManager.services.PortfolioSummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio")
public class PortfolioSummaryController {

    private final PortfolioSummaryService portfolioSummaryService;

    public PortfolioSummaryController(PortfolioSummaryService portfolioSummaryService) {
        this.portfolioSummaryService = portfolioSummaryService;
    }

    @GetMapping("/summary")
    public PortfolioSummary getSummary() {
        return portfolioSummaryService.getPortfolioSummary();
    }
}
