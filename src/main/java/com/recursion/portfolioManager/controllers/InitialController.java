package com.recursion.portfolioManager.controllers;

import com.recursion.portfolioManager.models.ApiResult;
import com.recursion.portfolioManager.models.ResponseApi;
import com.recursion.portfolioManager.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {

    @Autowired
    private RepoService repoService;

    @GetMapping("/api/{ticker}")
    public ApiResult getApi(@PathVariable String ticker){
        ApiResult apiResult= repoService.getApiData(ticker);
        return apiResult;
    }
    @GetMapping("/api/30days/{ticker}")
    public ResponseEntity save30Days(@PathVariable String ticker){
        repoService.save30DaysRepo(ticker);
        return ResponseEntity.ok().build();
    }
}
