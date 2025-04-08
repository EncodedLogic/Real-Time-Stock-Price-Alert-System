package com.practice.stock_price_alert_system.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class SecretsManager {

    private final Dotenv dotenv = Dotenv.load();

    public String getFinnhubApiKey(){
        return dotenv.get("FINNHUB_API_KEY");
    }

    public String getSpringAiOpenAiApiKey(){
        return dotenv.get("SPRING_AI_OPENAI_API_KEY");
    }

    public String getYahooApiKey() {
        return dotenv.get("YAHOO_API_KEY");
    }

    public String getYahooApiHost() {
        return dotenv.get("YAHOO_API_HOST");
    }


}
