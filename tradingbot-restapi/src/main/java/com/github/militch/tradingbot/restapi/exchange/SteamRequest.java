package com.github.militch.tradingbot.restapi.exchange;

import lombok.Data;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class SteamRequest {
    private static final AtomicInteger num = new AtomicInteger();
    public static SteamRequest create(){
        int id = num.incrementAndGet();
        return new SteamRequest(id);
    }
    private final Integer id;
    private String method;
    private String[] params;
    private SteamRequest(Integer id){
        this.id = id;
    }
}
