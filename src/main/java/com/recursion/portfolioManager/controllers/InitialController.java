package com.recursion.portfolioManager.controllers;

import com.recursion.portfolioManager.models.ResponseApi;
import com.recursion.portfolioManager.services.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitialController {

    @Autowired
    private RepoService repoService;

    @GetMapping("/api")
    public ResponseApi getApi(){
        return repoService.getApiData();
    }
}
