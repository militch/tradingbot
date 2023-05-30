package com.github.militch.tradingbot.restapi.exchange;

import com.google.gson.Gson;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class StreamListener extends WebSocketListener {
    private static final Gson g = new Gson();
    private final BinanceClient client;
    public StreamListener(BinanceClient client){
        this.client = client;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        client.connectDone();
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        StreamResponse resp = g.fromJson(text, StreamResponse.class);
        if (resp.getId() != null) {
            client.handleRequestMessage(resp);
            return;
        }
        BinanceEvent e = g.fromJson(text, BinanceEvent.class);
        if (e.getEvent() != null) {
            String eventName = e.getEvent();
            if (eventName.equals("trade")) {
                BinanceTradeEvent te = g.fromJson(text, BinanceTradeEvent.class);
                client.handleEvent(te);
            }else if(eventName.equals("kline")) {
                //log.info("kline..");
            }
        }
    }


    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        client.handleClose();
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        if (response == null) {
            log.warn("Stream handle failure: ", t);
            return;
        }
        ResponseBody body = response.body();
        if (body == null) {
            log.warn("Stream handle failure: ", t);
            return;
        }
        String stringbody = null;
        try {
            stringbody = body.string();
            log.info("onFailure: {} case: ",stringbody, t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("onClosed");
    }
}
