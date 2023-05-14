package com.github.militch.tradingbot.restapi;

import com.github.militch.tradingbot.restapi.exchange.BinanceClient;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Timer;

@SpringBootApplication
@MapperScan("com.github.militch.tradingbot.restapi.mapper")
public class TradingBotRestApiApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(TradingBotRestApiApplication.class);
    private static final Timer mainTimer = new Timer();
    private final TradeTrackingTask trackingTask;


    public static void main(String[] args) {
        SpringApplication.run(TradingBotRestApiApplication.class, args);
    }

    public TradingBotRestApiApplication(TradeTrackingTask trackingTask) {
        this.trackingTask = trackingTask;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("run start");
        mainTimer.schedule(trackingTask, 0, 1000);
    }
}