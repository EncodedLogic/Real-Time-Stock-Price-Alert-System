package com.practice.stock_price_alert_system.controller;

import com.practice.stock_price_alert_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<Map<String, Object>> getStockDetails(@PathVariable String symbol){
        return ResponseEntity.ok(stockService.fetchStockDetailsFromExternalApi(symbol));
    }
}
