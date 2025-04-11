package com.practice.stock_price_alert_system.service;

import com.practice.stock_price_alert_system.config.SecretsManager;
import com.practice.stock_price_alert_system.exception.GlobalException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

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

    @PostConstruct
    public void warmupCache() {
        System.out.println("Fetching top gainers cache at startup...");
        getTopGainersDetails();
    }


    @Cacheable("stockDetailsFinnhubCache")
    public Map<String, Object> fetchStockDetails(String symbol){
        return finnhubClient.get().uri(uriBuilder -> uriBuilder
                .path("/quote")
                .queryParam("symbol", symbol)
                .queryParam("token", secretsManager.getFinnhubApiKey())
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>(){})
                .block();
    }

    public Map<String,Object> getStockSymbols(){
        return finnhubClient.get().uri(uriBuilder -> uriBuilder
                .path("/stock/symbol")
                        .queryParam("exchange","US")
                        .queryParam("token",secretsManager.getFinnhubApiKey())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>(){})
                .block();
    }

    public List<Map<String, Object>> getTopGainersFromYahoo() {
        return yahooClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/market/v2/get-movers")
                        .queryParam("region", "US")
                        .queryParam("lang", "en-US")
                        .queryParam("count", 10)
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

    @Scheduled(fixedRate = 600000)
    @Cacheable(value = "topGainersFinnhubYahooCache")
    public List<Map<String, Object>> getTopGainersDetails(){
        List<Map<String,Object>> detailedCollection = getTopGainersFromYahoo();

       return detailedCollection.stream()
                .map(eachMap -> {
                    String stockSymbol =(String) eachMap.get("symbol");
                    if(stockSymbol!=null){
                        Map<String, Object> fetchedStockDetails = new HashMap<>(Map.of("symbol", stockSymbol));
                        Object resultObj = stockSearchBySymbol(stockSymbol).get("result");
                        if(resultObj instanceof List<?> resultList){
                            for(Object obj : resultList){
                                if(obj instanceof Map<?,?> item){
                                    String stockSym = (String)item.get("displaySymbol");
                                    if(stockSym.equalsIgnoreCase(stockSymbol)){
                                        fetchedStockDetails.put("companyName",item.get("description"));
                                        break;
                                    }
                                }
                            }
                        }
                        fetchedStockDetails.putAll(fetchStockDetails(stockSymbol));
                        System.out.println(">>> Fetching fresh top gainers");
                        return fetchedStockDetails;
                    }else{
                        throw new GlobalException("Bro this symbol doesn't exists");
                    }
                }).filter(Objects::nonNull).toList();
    }

    @Cacheable("stockSearchFinnhubCache")
    public Map<String,Object> stockSearchBySymbol(String symbol){
        return finnhubClient.get().uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q",symbol)
                        .queryParam("token",secretsManager.getFinnhubApiKey())
                        .build())
                .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {}).block();
    }

}