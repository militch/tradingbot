package com.github.militch.tradingbot.webstream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class StreamMessageHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(StreamMessageHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String id = session.getId();
        logger.info("coming: {}", id);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        logger.info("onMessage: {}", msg);
    }
}
