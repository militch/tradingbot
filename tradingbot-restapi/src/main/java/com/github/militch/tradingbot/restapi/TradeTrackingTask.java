package com.github.militch.tradingbot.restapi;

import com.github.militch.tradingbot.restapi.core.TradingSymbol;
import com.github.militch.tradingbot.restapi.exchange.BinanceClient;
import com.github.militch.tradingbot.restapi.exchange.BinanceKLine;
import com.github.militch.tradingbot.restapi.service.MarketKLineService;
import com.github.militch.tradingbot.restapi.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TradeTrackingTask extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(TradeTrackingTask.class);
    private static final long ONE_MINUTE = 1000 * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long ONE_YEAR = ONE_DAY * 365;
    private final BinanceClient client;
    private final MarketKLineService marketKLineService;
    private long lastTime;
    private final AtomicInteger ai = new AtomicInteger(0);
    public TradeTrackingTask(BinanceClient client, MarketKLineService marketKLineService){
        this.client = client;
        this.marketKLineService = marketKLineService;
    }

    private int fetch(long start){
        Map<String, String> params = new HashMap<>();
        params.put("symbol", TradingSymbol.BTCUSDT.getSymbol());
        params.put("interval", "1m");
        params.put("limit", "200");
        params.put("startTime", String.valueOf(start));
        List<BinanceKLine> gotList = client.getKLines(params);
        if (gotList == null || gotList.isEmpty()) {
            return 0;
        }
        int n = 0;
        for (BinanceKLine kl : gotList) {
            if (gotList.size() < 200) {
                if (n == gotList.size() - 1) {
                    break;
                }
            }
            n++;
            logger.info("got kl: {}", kl);
            marketKLineService.addBinanceKLine(kl, TradingSymbol.BTCUSDT);
            lastTime = kl.getCloseTime();
        }
        return n;
    }
    private void req(long start){
        while (true) {
            int n = fetch(start);
            start += ONE_MINUTE * n;
            if (n < 200) {
                break;
            }
        }
    }


    private static long currentTimestamp(){
        LocalDateTime now = LocalDateTime.now();
        now = now.withNano(0).withSecond(0);
        return TimeUtil.localDateTimeToTimestamp(now);
    }
    private final ExecutorService es = Executors.newFixedThreadPool(6);
    private void doSync() {
        if (!ai.compareAndSet(0, 1)){
            return;
        }
        long reqTime = System.currentTimeMillis();
        if (reqTime - lastTime < ONE_MINUTE) {
            ai.set(0);
            return;
        }
        if (lastTime == 0) {
            lastTime = marketKLineService.getLatestCloseTime();
            if (lastTime == 0) {
                long now = currentTimestamp();
                lastTime = now - ONE_HOUR;
            }
        }
        logger.info("fetch start: {}", lastTime);
        req(lastTime);
        ai.set(0);
    }
    @Override
    public void run() {
        es.execute(this::doSync);
    }
}
