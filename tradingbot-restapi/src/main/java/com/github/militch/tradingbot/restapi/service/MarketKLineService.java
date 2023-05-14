package com.github.militch.tradingbot.restapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.militch.tradingbot.restapi.core.TradingSymbol;
import com.github.militch.tradingbot.restapi.exchange.BinanceKLine;
import com.github.militch.tradingbot.restapi.model.MarketKLine;
import org.springframework.stereotype.Service;

public interface MarketKLineService extends IService<MarketKLine> {
    long getLatestCloseTime();
    boolean addBinanceKLine(BinanceKLine kLine, TradingSymbol symbol);
}
