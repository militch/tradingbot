package com.github.militch.tradingbot.restapi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class MarketKLineVO {
    private Integer id;
    private String exchange;
    private String symbol;
    private Integer decimals;
    private Long openTime;
    private Long closeTime;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal volume;
    private BigDecimal quoteVolume;
    private Long counts;
    private LocalDateTime createTime;
}
