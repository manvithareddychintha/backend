package com.recursion.portfolioManager.services;

import com.recursion.portfolioManager.models.AssetPrice;
import com.recursion.portfolioManager.repositories.AssetPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AssetPriceService {
    @Service
    public class PriceService {

        @Autowired
        private AssetPriceRepository priceRepository;

        public void savePrice(String symbol, BigDecimal price) {
            AssetPrice p = new AssetPrice();
            p.setSymbol(symbol);
            p.setPrice(price);
            p.setTimestamp(LocalDateTime.now());

            priceRepository.save(p);
        }
    }

}
