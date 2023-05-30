package com.github.militch.tradingbot.restapi.exchange;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Log4j2
public class BinanceClient {

    private final String apiEndpoint;
    private final String apiKey;
    private final String secretKey;
    private WebSocket webSocket;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public static final String DEFAULT_API_ENDPOINT = "https://api.binance.com";
    private static final String DEFAULT_STREAM_ENDPOINT = "wss://stream.binance.com";
    public static final String DATA_STREAM_ENDPOINT = "wss://data-stream.binance.com";
    private final OkHttpClient client;

    private final static Gson g = new Gson();

    public BinanceClient(){
        this(DEFAULT_API_ENDPOINT);
    }
    public BinanceClient(String apiEndpoint){
        this(null,null,apiEndpoint);
    }
    public BinanceClient(String apiKey, String secretKey){
        this(apiKey, secretKey, DEFAULT_API_ENDPOINT);
    }

    public BinanceClient(String apiKey, String secretKey, String apiEndpoint) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.apiEndpoint = apiEndpoint;
        SocketAddress sa = new InetSocketAddress("127.0.0.1",1080);
        Proxy httpProxy = new Proxy(Proxy.Type.SOCKS, sa);
        client = new OkHttpClient.Builder()
                .proxy(httpProxy)
                .build();
    }


    public long getTime() {
        Map<String, String> data = doReuqestGetStringMap("/api/v3/time");
        if (data == null) { return 0; }
        String serverTime = data.get("serverTime");
        return Long.parseLong(serverTime);
    }

    public Map<String, String> doReuqestGetStringMap(String path){
        String data = doRequestGetJsonString(path, null);
        if (data == null){ return null; }
        return g.fromJson(data, new TypeToken<Map<String, String>>(){}.getType());
    }

    public BinanceKLine getLatestKLine(String symbol, String interval) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", symbol);
        params.put("interval", interval);
        //params.put("startTime", "");
        //params.put("endTime", "");
        params.put("limit", "1");
        List<BinanceKLine> kLines = getKLines(params);
        if (kLines == null || kLines.isEmpty())
            return null;
        return kLines.stream().findFirst().get();
    }
    public List<BinanceKLine> getKLines(Map<String, String> params){
        String dataString = doRequestGetJsonString("/api/v3/klines", params);
        List<String[]> data = g.fromJson(dataString, new TypeToken<List<String[]>>(){}.getType());
        if (data == null || data.isEmpty())
            return null;
        return data.stream().filter((s)-> s.length >= 11).map((s)->{
            BinanceKLine kLine = new BinanceKLine();
            Long openTime = Long.parseLong(s[0]);
            kLine.setOpenTime(openTime);
            kLine.setOpenPrice(s[1]);
            kLine.setHighPrice(s[2]);
            kLine.setLowPrice(s[3]);
            kLine.setClosePrice(s[4]);
            kLine.setVolume(s[5]);
            Long closeTime = Long.parseLong(s[6]);
            kLine.setCloseTime(closeTime);
            kLine.setQuoteVolume(s[7]);
            Long count = Long.parseLong(s[8]);
            kLine.setCount(count);
            return kLine;
        }).collect(Collectors.toList());
    }
    public String doRequestGetJsonString(String path, Map<String, String> params) {
        String url = apiEndpoint + path;
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null)
            return null;
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        urlBuilder.fragment(url);
        if (params != null && !params.isEmpty()) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                urlBuilder.addQueryParameter(param.getKey(),param.getValue());
            }
        }
        Request request = new Request.Builder().url(urlBuilder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                ResponseBody rb = response.body();
                String body = null;
                if (rb != null){
                    body = rb.string();
                }
                log.warn("Failed request: status({}) body: {}", response.code(), body);
                return null;
            }
            ResponseBody rb = response.body();
            if (rb == null) {
                return null;
            }
            return rb.string();
        }catch (IOException e) {
            log.warn("Failed request: ", e);
            return null;
        }
    }

    private long timeout;

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    private boolean sendRequest(SteamRequest request){
        Map<String, Object> map = new HashMap<>();
        map.put("method", request.getMethod());
        map.put("params", request.getParams());
        map.put("id", request.getId());
        String jsonString = g.toJson(map);
        log.info("send stream request: {}", jsonString);
        return webSocket.send(jsonString);
    }

    private final Map<Integer, ResponseFuture> pending = new LinkedHashMap<>();
    private final Lock pendingLock = new ReentrantLock();
    private StreamResponse streamRequest(String method, String[] params) throws Exception {
        connect();
        SteamRequest req = SteamRequest.create();
        req.setMethod(method);
        req.setParams(params);
        if (timeout == 0) {
            timeout = 30000;
        }
        ResponseFuture future = new ResponseFuture(req, timeout);
        int id = req.getId();
        try {
            pendingLock.lock();
            pending.put(id, future);
        }finally {
            pendingLock.unlock();
        }
        if(!sendRequest(req)){
            throw new Exception("Failed send request");
        }
        return future.waitResponse();
    }
    private SteamHandler steamHandler;

    public void setSteamHandler(SteamHandler steamHandler) {
        this.steamHandler = steamHandler;
    }
    public void subscribe(String... params) throws Exception {
        StreamResponse resp = streamRequest("SUBSCRIBE", params);
        StreamError error = resp.getError();
        if (error != null) {
            throw new Exception(error.getMsg());
        }
    }
    public void ping() throws Exception {
        StreamResponse resp = streamRequest("ping", null);
    }
    public void handleRequestMessage(StreamResponse response){
        try {
            int id = response.getId();
            pendingLock.lock();
            if (!pending.containsKey(id)) {
                pendingLock.unlock();
                return;
            }
            ResponseFuture rf = pending.get(id);
            rf.process(response);
        }finally {
            pendingLock.unlock();
        }
    }
    public void handleEvent(BinanceEvent event){
        if (steamHandler == null) {
            return;
        }
        if (event instanceof BinanceTradeEvent) {
            steamHandler.onTrade((BinanceTradeEvent) event);
        }
    }
    public void connect() throws InterruptedException {
        if (webSocket == null) {
            StreamListener listener = new StreamListener(this);
            Request request = new Request.Builder().url(DATA_STREAM_ENDPOINT + "/ws").build();
            webSocket =  client.newWebSocket(request, listener);
        }
        if (countDownLatch.getCount() > 0) {
            countDownLatch.await();
        }
    }
    public void handleClose(){
        if (webSocket != null) {
            webSocket = null;
        }
        if (countDownLatch.getCount() == 0) {
            countDownLatch = new CountDownLatch(1);
        }
    }
    public void connectDone(){
        countDownLatch.countDown();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
