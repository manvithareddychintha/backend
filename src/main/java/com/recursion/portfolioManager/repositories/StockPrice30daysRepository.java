package com.recursion.portfolioManager.repositories;

import com.recursion.portfolioManager.models.StockPrice30Days;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockPrice30daysRepository extends JpaRepository<StockPrice30Days,Long> {

    public void deleteBySymbol(String symbol);

    Optional<StockPrice30Days> findTopBySymbolOrderByLocalDateDesc(String symbol);

    List<StockPrice30Days> findBySymbolAndLocalDateAfter(String symbol, LocalDate dateAdded);
    @Modifying
    @Transactional
    @Query("DELETE FROM StockPrice30Days")
    void deleteAllData();
}
