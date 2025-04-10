package com.practice.stock_price_alert_system.controller;

import com.practice.stock_price_alert_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @GetMapping("/stocks/{symbol}")
    public ResponseEntity<Map<String, Object>> getStockDetails(@PathVariable String symbol){
        return ResponseEntity.ok(stockService.fetchStockDetails(symbol));
    }

    @GetMapping("/")
    public ResponseEntity<List<Map<String, Object>>> getTopGainersFromYahoo() {
        try {
            List<Map<String, Object>> gainers = stockService.getTopGainersDetails();
            if (gainers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            return ResponseEntity.ok(gainers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

}
