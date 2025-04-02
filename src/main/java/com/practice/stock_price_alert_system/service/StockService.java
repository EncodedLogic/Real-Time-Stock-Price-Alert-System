package com.practice.stock_price_alert_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class StockService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${finnhub.api.key}")
    private String api_key;

    private static final String STOCK_DETAILS_URL = "https://finnhub.io/api/v1/quote?symbol={symbol}&token={apiKey}";

    public Map<String, Object> fetchStockDetailsFromExternalApi(String symbol){
        String url = STOCK_DETAILS_URL.replace("{symbol}",symbol).replace("{api_key}",api_key);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();  // Returns JSON as a Map (key-value pairs)
        } else {
            throw new RuntimeException("Failed to fetch stock details for " + symbol);
        }
    }

}