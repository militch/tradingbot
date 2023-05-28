package com.github.militch.tradingbot.restapi;

import com.github.militch.tradingbot.restapi.exchange.BinanceClient;
import com.github.militch.tradingbot.restapi.exchange.SteamHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public BinanceClient binanceClient(SteamHandler steamHandler) {
        BinanceClient binanceClient = new BinanceClient();
        binanceClient.setSteamHandler(steamHandler);
        return binanceClient;
    }

    @Bean
    public SteamHandler binanceSteamHandler(){
        return new BinanceSteamHandlerImpl();
    }
}
