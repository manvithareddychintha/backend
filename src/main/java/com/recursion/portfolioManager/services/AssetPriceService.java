package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.DTO.SymbolType;
import com.recursion.portfolioManager.models.*;
import com.recursion.portfolioManager.models.other.AssetType;
import com.recursion.portfolioManager.repositories.AssetPriceRepository;
import com.recursion.portfolioManager.repositories.HoldingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AssetPriceService {


        @Autowired
        private AssetPriceRepository priceRepository;

        @Autowired
        private HoldingsRepository holdingsRepository;

        @Autowired
        private RestTemplate restTemplate;
        static String url="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";

        @Value("${alphavantage.api.key}")
        private String apikey;

        public ResponseEntity refreshPrices(){
            List<SymbolType> symbolTypeList = holdingsRepository.findDistinctSymbolAndType();

            for (SymbolType s : symbolTypeList) {
                System.out.println(s.getSymbol()+" "+s.getAssetType());
                if(s.getAssetType()== AssetType.EQUITY) {
                    String url =
                            "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY" +
                                    "&symbol=" + s.getSymbol() +
                                    "&apikey=" + apikey;
                    System.out.println(url);
                    String response = restTemplate.getForObject(url, String.class);

                    BigDecimal close = extractLatestClose(response);

                    savePrice(s.getSymbol(), close);
                }else{
                    savePrice(s.getSymbol(),new BigDecimal(1));
                }
            }


            return ResponseEntity.ok().build();
        }

        private BigDecimal extractLatestClose(String json)  {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

//            JsonNode root = mapper.readTree(json);



            ResponseApi data = mapper.treeToValue(root, ResponseApi.class);

            //return new ApiResult(true, data);
            ///JsonNode timeSeries = root.get("Time Series (Daily)");

            // get most recent date
            Map<LocalDate, TimeSeriesPerDay> dates = data.getTimeSeriesDaily();
            if(dates==null)return new BigDecimal(300);
//            LocalDate latestDate = dates.get(0);

            LocalDate latestDate = dates.keySet()
                    .stream()
                    .max(LocalDate::compareTo)
                    .orElseThrow(() -> new RuntimeException("No price data found"));

            String closeStr = dates.get(latestDate).getClose();


            return new BigDecimal(closeStr);
        }


    public void savePrice(String symbol, BigDecimal price) {
        // Check if the price already exists for the given symbol
        Optional<AssetPrice> existingPrice = priceRepository.findBySymbol(symbol);

        // If it exists, delete the existing price
        existingPrice.ifPresent(priceRepository::delete);

        // Create a new AssetPrice object
        AssetPrice p = new AssetPrice();
        p.setSymbol(symbol);
        p.setPrice(price);
        p.setTimestamp(LocalDateTime.now());

        // Save the new price
        priceRepository.save(p);
    }
    }


