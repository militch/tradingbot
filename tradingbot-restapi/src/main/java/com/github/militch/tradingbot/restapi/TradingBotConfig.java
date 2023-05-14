package com.github.militch.tradingbot.restapi;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("tradingbot")
@Component
public class TradingBotConfig {
    private String httpProxy;
    private String httpsProxy;
}
