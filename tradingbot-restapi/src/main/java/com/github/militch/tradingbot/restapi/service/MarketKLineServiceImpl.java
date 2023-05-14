package com.github.militch.tradingbot.restapi.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.militch.tradingbot.restapi.core.TradingSymbol;
import com.github.militch.tradingbot.restapi.exchange.BinanceKLine;
import com.github.militch.tradingbot.restapi.mapper.MarketKLineMapper;
import com.github.militch.tradingbot.restapi.model.MarketKLine;
import com.github.militch.tradingbot.restapi.utils.NumberUtil;
import com.github.militch.tradingbot.restapi.utils.TimeUtil;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
public class MarketKLineServiceImpl extends ServiceImpl<MarketKLineMapper, MarketKLine> implements MarketKLineService {
    private boolean existsKLineByOpenTime(Long openTime){
        LambdaQueryWrapper<MarketKLine> lineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        lineLambdaQueryWrapper.eq(MarketKLine::getOpenTime, openTime);
        return count(lineLambdaQueryWrapper) > 0;
    }

    @Override
    public long getLatestCloseTime() {
        LambdaQueryWrapper<MarketKLine> lineLambdaQueryWrapper = new LambdaQueryWrapper<>();
        lineLambdaQueryWrapper.orderByDesc(MarketKLine::getCloseTime).last("LIMIT 1");
        MarketKLine item = getOne(lineLambdaQueryWrapper);
        if (item == null) return 0;
        return item.getCloseTime();
    }

    @Override
    public boolean addBinanceKLine(BinanceKLine kLine, TradingSymbol symbol) {
        if (kLine == null) return false;
        Long openTime = kLine.getOpenTime();
        if (openTime == null) return false;
        if (existsKLineByOpenTime(openTime)) return false;
        MarketKLine marketKLine = new MarketKLine();
        marketKLine.setExchange("BINANCE");
        marketKLine.setSymbol(symbol.getSymbol());
        int decimals = symbol.getDecimals();
        marketKLine.setDecimals(decimals);
        marketKLine.setCreateTime(LocalDateTime.now());
        marketKLine.setOpenTime(kLine.getOpenTime());
        marketKLine.setCloseTime(kLine.getCloseTime());
        BigInteger openPrice = NumberUtil.scalePrice(kLine.getOpenPrice(), decimals);
        marketKLine.setOpenPrice(openPrice);
        BigInteger closePrice = NumberUtil.scalePrice(kLine.getClosePrice(), decimals);
        marketKLine.setClosePrice(closePrice);
        BigInteger highPrice = NumberUtil.scalePrice(kLine.getHighPrice(), decimals);
        marketKLine.setHighPrice(highPrice);
        BigInteger lowPrice = NumberUtil.scalePrice(kLine.getLowPrice(), decimals);
        marketKLine.setLowPrice(lowPrice);
        BigInteger volume = NumberUtil.scalePrice(kLine.getVolume(), decimals);
        marketKLine.setVolume(volume);
        BigInteger quoteVolume = NumberUtil.scalePrice(kLine.getQuoteVolume(), decimals);
        marketKLine.setQuoteVolume(quoteVolume);
        marketKLine.setCounts(kLine.getCount());
        return save(marketKLine);
    }
}
