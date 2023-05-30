package com.github.militch.tradingbot.restapi.exchange;

public interface SteamHandler {
    void onTrade(BinanceTradeEvent event);
}
