package com.github.militch.tradingbot.restapi.exchange;

import com.google.gson.Gson;
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

public class StreamListener extends WebSocketListener {
    private static Logger logger = LoggerFactory.getLogger(StreamListener.class);
    private static Gson g = new Gson();
    private BinanceClient client;
    public StreamListener(BinanceClient client){
        this.client = client;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        //client.requestSubscribe(new String[]{"btcusdt@trade"});
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        logger.info("onMsg: {}", text);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        logger.info("onMsg raw");
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        logger.info("onClosing");
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        ResponseBody body = response.body();
        String stringbody = null;
        try {
            stringbody = body.string();
            logger.info("onFailure: {} case: ",stringbody, t);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        logger.info("onClosed");
    }
}
