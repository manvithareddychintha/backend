package com.recursion.portfolioManager.repositories;

import com.recursion.portfolioManager.DTO.HoldingWithPriceDTO;
import com.recursion.portfolioManager.DTO.InvestedValueDTO;
import com.recursion.portfolioManager.DTO.SymbolType;
import com.recursion.portfolioManager.models.Holdings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HoldingsRepository extends JpaRepository<Holdings, Long> {

    @Query("SELECT DISTINCT new com.recursion.portfolioManager.DTO.SymbolType(h.symbol, h.assetType) FROM Holdings h")
    List<SymbolType> findDistinctSymbolAndType();

    @Query("""
        SELECT new com.recursion.portfolioManager.DTO.HoldingWithPriceDTO(
            h.id,
            h.symbol,
            h.assetName,
            h.assetType,
            h.quantity,
            h.avgBuyPrice,
            p.price,
            (p.price - h.avgBuyPrice) * h.quantity
        )
        FROM Holdings h
        JOIN AssetPrice p ON h.symbol = p.symbol
        WHERE p.timestamp = (
            SELECT MAX(p2.timestamp)
            FROM AssetPrice p2
            WHERE p2.symbol = h.symbol
        )
    """)
    List<HoldingWithPriceDTO> findHoldingsWithLatestPriceAndProfit();

    Optional<Holdings> findBySymbolIgnoreCase(String symbol);

    @Query("""
SELECT 
    h.assetType AS assetType,
    SUM(h.quantity * ap.price) AS totalValue
FROM Holdings h
JOIN AssetPrice ap 
    ON ap.symbol = h.symbol
WHERE ap.timestamp = (
    SELECT MAX(a2.timestamp)
    FROM AssetPrice a2
    WHERE a2.symbol = h.symbol
)
GROUP BY h.assetType
""")
    List<HoldingValue> findAllocationByAssetType();


    @Query("""
        SELECT
            h.quantity AS quantity,
            h.avgBuyPrice AS avgBuyPrice,
            p.price AS latestPrice
        FROM Holdings h
        JOIN AssetPrice p ON h.symbol = p.symbol
        WHERE p.timestamp = (
            SELECT MAX(p2.timestamp)
            FROM AssetPrice p2
            WHERE p2.symbol = h.symbol
        )
    """)
    List<HoldingValue> findHoldingValues();

        @Query("""
    SELECT new com.recursion.portfolioManager.DTO.InvestedValueDTO(
        h.symbol,
        (h.avgBuyPrice * h.quantity)
    )
    FROM Holdings h
    """)
    List<InvestedValueDTO> findInvestedValueBySymbol();

    @Query("""
    SELECT new com.recursion.portfolioManager.DTO.InvestedValueDTO(
        h.symbol,
        SUM(h.avgBuyPrice * h.quantity)
    )
    FROM Holdings h
    GROUP BY h.symbol
    """)
    List<InvestedValueDTO> findTotalInvestedValueBySymbol();


    @Transactional
    @Modifying
    @Query("update Holdings h set h.quantity = ?1 where upper(h.symbol) = upper(?2)")
    void updateQuantityBySymbolIgnoreCase(BigDecimal quantity, String symbol);
}
