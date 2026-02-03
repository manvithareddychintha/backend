package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.models.ApiResult;
import com.recursion.portfolioManager.models.ResponseApi;
import com.recursion.portfolioManager.models.other.AssetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class MarketApiService {


    @Autowired
    RestTemplate restTemplate;

    static String url="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";

    @Value("${alphavantage.api.key}")
    private String apikey;




    public boolean isValidSymbol(String symbol, AssetType type) {
        // call API with symbol

        String url2 = url+symbol+"&apikey="+apikey;
        String apiResponseString=restTemplate.getForObject(url2, String.class);

//        System.out.println(url2);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(apiResponseString);

        if (root.has("Error Message")) {
            String msg = root.get("Error Message").asText();

            return false;
        }

        ResponseApi data = mapper.treeToValue(root, ResponseApi.class);

        // if API returns "Error Message" → false
        return true;
    }
}
