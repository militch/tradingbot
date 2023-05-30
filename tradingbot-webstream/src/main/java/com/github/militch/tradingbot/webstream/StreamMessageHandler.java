package com.github.militch.tradingbot.webstream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class StreamMessageHandler extends TextWebSocketHandler {
    public class MessageHandler extends TimerTask {
        @Override
        public void run() {
            try {
                sendMsg();
            } catch (IOException e) {
                logger.warn("send error: ", e);
            }
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(StreamMessageHandler.class);
    private final Map<String,WebSocketSession> sessionMap = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private final Timer t = new Timer();

    public StreamMessageHandler(){
        t.schedule(new MessageHandler(), 0, 1000);
    }


    private void sendMsg() throws IOException {
        readLock.lock();
        try {
            TextMessage tm = new TextMessage("hello");
            for (Map.Entry<String, WebSocketSession> entry : sessionMap.entrySet()) {
                WebSocketSession client = entry.getValue();
                client.sendMessage(tm);
            }
        }finally {
            readLock.unlock();
        }
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String id = session.getId();
        writeLock.lock();
        try{
            sessionMap.put(id, session);
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        logger.info("onMessage: {}", msg);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,@NonNull CloseStatus status) throws Exception {
        String id = session.getId();
        writeLock.lock();
        try {
            sessionMap.remove(id);
        } finally {
            writeLock.unlock();
        }
    }
}
