package com.github.militch.tradingbot.restapi;

import com.github.militch.tradingbot.restapi.exchange.BinanceTradeEvent;
import com.github.militch.tradingbot.restapi.exchange.SteamHandler;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;

@Log4j2
public class BinanceSteamHandlerImpl implements SteamHandler {
    @Override
    public void onTrade(BinanceTradeEvent event) {
        log.info("onEvent: {}", event);
    }
}
