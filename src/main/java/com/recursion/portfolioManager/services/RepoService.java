package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.models.ResponseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RepoService {

    @Autowired
    RestTemplate restTemplate;

    static String url="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AMZN&apikey=";

    @Value("${alphavantage.api.key}")
    private String apikey;

    public ResponseApi getApiData()
    {

        return restTemplate.getForObject(url+apikey, ResponseApi.class);
    }

}
