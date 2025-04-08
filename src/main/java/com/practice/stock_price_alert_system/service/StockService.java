package com.practice.stock_price_alert_system.service;

import com.practice.stock_price_alert_system.config.SecretsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class StockService {

    private final WebClient finnhubClient;
    private final WebClient yahooClient;
    private final SecretsManager secretsManager;

    @Autowired
    public StockService(WebClient.Builder webClientBuilder, SecretsManager secretsManager) {
        this.finnhubClient = webClientBuilder.baseUrl("https://finnhub.io/api/v1").build();
        this.yahooClient = webClientBuilder.baseUrl("https://yh-finance.p.rapidapi.com")
                .defaultHeader("X-RapidAPI-Host", secretsManager.getYahooApiHost())
                .defaultHeader("X-RapidAPI-Key", secretsManager.getYahooApiKey())
                .build();
        this.secretsManager = secretsManager;
    }


    public Map<String, Object> fetchStockDetailsFromExternalApi(String symbol){
        return finnhubClient.get().uri(uriBuilder -> uriBuilder
                .path("/quote")
                .queryParam("symbol", symbol)
                .queryParam("token", secretsManager.getFinnhubApiKey())
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>(){})
                .block();
    }

    public Map<String,String> getStockSymbols(){
        return finnhubClient.get().uri(uriBuilder -> uriBuilder
                .path("/stock/symbol")
                        .queryParam("exchange","US")
                        .queryParam("token",secretsManager.getFinnhubApiKey())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,String>>(){})
                .block();
    }

    public List<Map<String, Object>> getTopGainersFromYahoo() {
        return yahooClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/market/v2/get-movers")
                        .queryParam("region", "US")
                        .queryParam("lang", "en-US")
                        .queryParam("count", 20)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> {
                    Map<String, Object> finance = (Map<String, Object>) response.get("finance");
                    List<Map<String, Object>> result = (List<Map<String, Object>>) finance.get("result");

                    return result.stream()
                            .filter(r -> "Day Gainers".equals(r.get("title")))
                            .findFirst()
                            .map(r -> (List<Map<String, Object>>) r.get("quotes"))
                            .orElse(Collections.emptyList());
                })
                .block();
    }
}