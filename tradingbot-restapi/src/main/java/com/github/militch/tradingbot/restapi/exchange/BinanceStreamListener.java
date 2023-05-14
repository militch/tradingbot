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

public class BinanceStreamListener extends WebSocketListener {
    private static Logger logger = LoggerFactory.getLogger(BinanceStreamListener.class);
    private static Gson g = new Gson();

    public void subscribe(String name, SubscribeCallback callback){
    }

    private static boolean jsonRequest(WebSocket webSocket, long id, String method, String[] params){
        Map<String, Object> map = new HashMap<>();
        map.put("method", method);
        map.put("params", params);
        map.put("id", id);
        String jsonString = g.toJson(map);
        return webSocket.send(jsonString);
    }

    private static boolean requestSubscribe(WebSocket webSocket, String[] params){
        return jsonRequest(webSocket, 1, "SUBSCRIBE", params);
    }
    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        logger.info("onOpen");
        //requestSubscribe(webSocket, new String[] {"btcusdt@kline_1m"});
        requestSubscribe(webSocket, new String[] {"btcusdt@trade"});
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
