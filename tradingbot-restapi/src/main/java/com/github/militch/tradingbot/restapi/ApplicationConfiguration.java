package com.github.militch.tradingbot.restapi;

import com.github.militch.tradingbot.restapi.exchange.BinanceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public BinanceClient binanceClient() {
        return new BinanceClient();
    }
}
