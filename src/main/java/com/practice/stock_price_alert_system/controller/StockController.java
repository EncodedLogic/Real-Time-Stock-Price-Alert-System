package com.practice.stock_price_alert_system.controller;

import com.practice.stock_price_alert_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    public Map<String, Object> getStockDetails(@RequestParam String symbol){
        return stockService.fetchStockDetailsFromExternalApi(symbol);
    }
}
