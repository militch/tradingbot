package com.github.militch.tradingbot.restapi.exchange;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BinanceTradeEvent extends BinanceEvent {
    @SerializedName("s")
    private String symbol;
    @SerializedName("t")
    private String tradeId;
    @SerializedName("p")
    private String price;
    @SerializedName("q")
    private String quantity;
    @SerializedName("b")
    private String buyer;
    @SerializedName("a")
    private String seller;
    @SerializedName("T")
    private Long time;
    @SerializedName("m")
    private Boolean marketMarker;
}
