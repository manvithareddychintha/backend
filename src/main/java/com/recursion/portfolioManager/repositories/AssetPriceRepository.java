package com.recursion.portfolioManager.repositories;

import com.recursion.portfolioManager.models.AssetPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetPriceRepository extends JpaRepository<AssetPrice, Long> {
    AssetPrice findTopBySymbolOrderByTimestampDesc(String symbol);
}
