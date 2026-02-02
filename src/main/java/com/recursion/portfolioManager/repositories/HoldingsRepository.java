package com.recursion.portfolioManager.repositories;

import com.recursion.portfolioManager.models.Holdings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoldingsRepository extends JpaRepository<Holdings,Long> {

}
