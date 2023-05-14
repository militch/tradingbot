package com.github.militch.tradingbot.webstream;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final StreamMessageHandler streamMessageHandler;

    public WebSocketConfig(StreamMessageHandler streamMessageHandler) {
        this.streamMessageHandler = streamMessageHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(streamMessageHandler, "/").setAllowedOrigins("*");
    }
}
