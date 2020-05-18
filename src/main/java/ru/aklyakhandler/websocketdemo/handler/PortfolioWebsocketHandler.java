package ru.aklyakhandler.websocketdemo.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import ru.aklyakhandler.websocketdemo.service.WebSocketHandlerService;

@Component
@RequiredArgsConstructor
public class PortfolioWebsocketHandler implements WebSocketHandler {
    private final WebSocketHandlerService webSocketHandlerService;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return webSocketHandlerService.handle(session);
    }

}
