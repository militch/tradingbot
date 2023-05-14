package com.github.militch.tradingbot.restapi.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("market_kline")
public class MarketKLine extends BaseModel {
    /**
     * 交易所
     */
    @TableField("exchange")
    private String exchange;
    /**
     * 交易对符号
     */
    @TableField("symbol")
    private String symbol;
    /**
     * 小数精度
     */
    @TableField("decimals")
    private Integer decimals;
    /**
     * 开盘时间
     */
    @TableField("open_time")
    private Long openTime;
    /**
     * 收盘时间
     */
    @TableField("close_time")
    private Long closeTime;
    /**
     * 开盘价
     */
    @TableField("open_price")
    private BigInteger openPrice;
    /**
     * 收盘价
     */
    @TableField("close_price")
    private BigInteger closePrice;
    /**
     * 最高价
     */
    @TableField("high_price")
    private BigInteger highPrice;
    /**
     * 最低价
     */
    @TableField("low_price")
    private BigInteger lowPrice;
    /**
     * 成交量
     */
    @TableField("volume")
    private BigInteger volume;
    /**
     * 成交额
     */
    @TableField("quote_volume")
    private BigInteger quoteVolume;
    /**
     * 成交笔数
     */
    @TableField("counts")
    private Long counts;

}
