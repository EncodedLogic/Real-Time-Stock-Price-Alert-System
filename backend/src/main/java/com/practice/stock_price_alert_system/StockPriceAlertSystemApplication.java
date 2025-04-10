package com.practice.stock_price_alert_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StockPriceAlertSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockPriceAlertSystemApplication.class, args);
	}
}
