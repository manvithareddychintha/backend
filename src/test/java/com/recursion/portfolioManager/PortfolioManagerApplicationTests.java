package com.recursion.portfolioManager;

import com.recursion.portfolioManager.services.AssetPriceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PortfolioManagerApplicationTests {

	@Autowired
	AssetPriceService assetPriceService;
	@Test
	void Refresh() {

	}

}
