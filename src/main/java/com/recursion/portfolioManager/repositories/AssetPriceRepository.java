package com.recursion.portfolioManager.repositories;

import com.recursion.portfolioManager.models.AssetPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetPriceRepository extends JpaRepository<AssetPrice, Long> {
    AssetPrice findTopBySymbolOrderByTimestampDesc(String symbol);
    Optional<AssetPrice> findBySymbol(String symbol);


}
