package com.github.militch.tradingbot.restapi.exchange;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BinanceEvent {
    @SerializedName("e")
    private String event;
    @SerializedName("E")
    private String eventTime;
}
