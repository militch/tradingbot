package com.github.militch.tradingbot.restapi.exchange;

import lombok.Data;

@Data
public class ResponseFuture {
    private final SteamRequest request;
    private StreamResponse response;
    private final long timeout;

    public StreamResponse waitResponse() {
        return null;
    }
}
