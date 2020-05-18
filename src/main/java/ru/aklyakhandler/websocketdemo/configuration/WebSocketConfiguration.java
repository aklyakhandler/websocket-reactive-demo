package ru.aklyakhandler.websocketdemo.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import ru.aklyakhandler.websocketdemo.handler.PortfolioWebsocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class WebSocketConfiguration {
    private final PortfolioWebsocketHandler portfolioWebsocketHandler;

    @Bean
    public HandlerMapping webSocketHandlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/portfolio/{clientId}", portfolioWebsocketHandler);
//        map.put("/portfolio", portfolioWebsocketHandler);

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
