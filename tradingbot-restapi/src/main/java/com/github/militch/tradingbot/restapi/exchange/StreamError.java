package com.github.militch.tradingbot.restapi.exchange;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StreamError {
    private Integer code;
    private String msg;
}
