package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.models.ApiResult;
import com.recursion.portfolioManager.models.ResponseApi;
import com.recursion.portfolioManager.models.StockPrice30Days;
import com.recursion.portfolioManager.models.TimeSeriesPerDay;
import com.recursion.portfolioManager.repositories.StockPrice30daysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RepoService {

    @Autowired
    RestTemplate restTemplate;

    static String url="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AMZN&apikey=";

    @Value("${alphavantage.api.key}")
    private String apikey;


    @Autowired
    StockPrice30daysRepository stockPrice30daysRepository;

    public ApiResult getApiData(String ticker)
    {
        String url2 = url+ticker+"&apikey="+apikey;
        String apiResponseString=restTemplate.getForObject(url2, String.class);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(apiResponseString);

        if (root.has("Error Message")) {
            String msg = root.get("Error Message").asText();

            return new ApiResult(false, "Not a valid symbol used");
        }

        ResponseApi data = mapper.treeToValue(root, ResponseApi.class);

        return new ApiResult(true, data);

    }

    public void save30DaysRepo(String ticker)
    {

        String url2 = url+ticker+"&apikey="+apikey;
        String apiResponseString=restTemplate.getForObject(url2, String.class);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(apiResponseString);

        if (root.has("Error Message")) {
            String msg = root.get("Error Message").asText();
            return;

        }

        ResponseApi data = mapper.treeToValue(root, ResponseApi.class);

        String symbol = data.getMetaData().getSymbol();

        Map<LocalDate, TimeSeriesPerDay> series = data.getTimeSeriesDaily();

// sort dates DESC and take 30
        List<Map.Entry<LocalDate,TimeSeriesPerDay>> last30=data.getTimeSeriesDaily().entrySet().stream()
                .sorted(Map.Entry.<LocalDate, TimeSeriesPerDay>comparingByKey().reversed())
                .limit(30).toList();


        List<StockPrice30Days> prices = new ArrayList<>();

        for (Map.Entry<LocalDate, TimeSeriesPerDay> entry : last30) {
            StockPrice30Days sp = new StockPrice30Days();
            sp.setSymbol(symbol);
            sp.setLocalDate(entry.getKey());
            sp.setClosePrice(new BigDecimal(entry.getValue().getClose()));
            prices.add(sp);


        }

        stockPrice30daysRepository.saveAll(prices);

    }

}
