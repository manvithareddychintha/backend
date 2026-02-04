package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.DTO.SymbolType;
import com.recursion.portfolioManager.models.*;
import com.recursion.portfolioManager.models.other.AssetType;
import com.recursion.portfolioManager.repositories.AssetPriceRepository;
import com.recursion.portfolioManager.repositories.HoldingsRepository;
import com.recursion.portfolioManager.repositories.StockPrice30daysRepository;
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
        RepoService repoService;

        @Autowired
        private AssetPriceRepository priceRepository;

        @Autowired
        private StockPrice30daysRepository stockPrice30daysRepository;

        @Autowired
        private HoldingsRepository holdingsRepository;

        @Autowired
        private RestTemplate restTemplate;
        static String url="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=";

        @Value("${alphavantage.api.key}")
        private String apikey;

    public ResponseEntity refreshPrices() throws Exception{
        List<SymbolType> symbolTypeList = holdingsRepository.findDistinctSymbolAndType();

        LocalDateTime now = LocalDateTime.now();

        for (SymbolType s : symbolTypeList) {
            System.out.println("Processing Symbol: " + s.getSymbol() + " of Type: " + s.getAssetType());

            if (s.getAssetType() == AssetType.EQUITY) {
                Optional<StockPrice30Days> latestPriceOpt = stockPrice30daysRepository.findTopBySymbolOrderByLocalDateDesc(s.getSymbol());

                if (latestPriceOpt.isPresent()) {
                    StockPrice30Days latestPrice = latestPriceOpt.get();

                    if (latestPrice.getLocalDate().isBefore(now.minusDays(1).toLocalDate())) {

                        System.out.println("Data is stale for symbol " + s.getSymbol() + ", fetching new data...");
                        fetchAndSavePrice(s);
                    } else {
                        System.out.println("Data is up-to-date for symbol " + s.getSymbol() + ", skipping API call.");
                    }
                } else {

                    System.out.println("No data found for symbol " + s.getSymbol() + ", fetching from API...");
                    fetchAndSavePrice(s);
                }
            } else {
                savePrice(s.getSymbol(), new BigDecimal(1));
            }
        }

        return ResponseEntity.ok("Prices refreshed successfully");
    }

    private void fetchAndSavePrice(SymbolType s) throws Exception{
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY" +
                "&symbol=" + s.getSymbol() +
                "&apikey=" + apikey;
        System.out.println("API URL: " + url);
        Thread.sleep(2000);

        String response = restTemplate.getForObject(url, String.class);

        BigDecimal close = extractLatestClose(response);

        savePrice(s.getSymbol(), close);
    }


    private BigDecimal extractLatestClose(String json)  {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

//            JsonNode root = mapper.readTree(json);



            ResponseApi data = mapper.treeToValue(root, ResponseApi.class);


            repoService.save30DaysRepoFromRefresh(data);
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
        Optional<AssetPrice> existingPrice = priceRepository.findBySymbol(symbol);

        existingPrice.ifPresent(priceRepository::delete);

        AssetPrice p = new AssetPrice();
        p.setSymbol(symbol);
        p.setPrice(price);
        p.setTimestamp(LocalDateTime.now());

        priceRepository.save(p);
    }
    }


