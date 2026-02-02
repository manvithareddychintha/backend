package com.recursion.portfolioManager.repositories;

import com.recursion.portfolioManager.models.StockPrice30Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.stereotype.Repository;

@Repository
public interface StockPrice30daysRepository extends JpaRepository<StockPrice30Days,Long> {

}
