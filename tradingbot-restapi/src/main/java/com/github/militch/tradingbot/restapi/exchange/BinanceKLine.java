package com.github.militch.tradingbot.restapi.exchange;

import lombok.Data;

@Data
public class BinanceKLine {
    /**
     * 开盘时间
     */
    private Long openTime;
    /**
     * 收盘时间
     */
    private Long closeTime;
    /**
     * 开盘价
     */
    private String openPrice;
    /**
     * 收盘价
     */
    private String closePrice;
    /**
     * 最高价
     */
    private String highPrice;
    /**
     * 最低价
     */
    private String lowPrice;
    /**
     * 成交量
     */
    private String volume;
    /**
     * 成交额
     */
    private String quoteVolume;
    /**
     * 成交笔数
     */
    private Long count;
    private String bidPrice;
    private String bidQty;
}
