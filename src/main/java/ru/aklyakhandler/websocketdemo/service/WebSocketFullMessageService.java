package ru.aklyakhandler.websocketdemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import ru.aklyakhandler.websocketdemo.exception.WebsocketServiceException;
import ru.aklyakhandler.websocketdemo.protocol.WebSocketCustomMessage;

@Service
@RequiredArgsConstructor
public class WebSocketFullMessageService {
    private final ObjectMapper objectMapper;

    public Mono<WebSocketMessage> getFullMessage(WebSocketCustomMessage webSocketCustomMessage, WebSocketSession webSocketSession) {
        return Mono.fromCallable(() ->
                webSocketSession.textMessage(produceTextMessageFull(webSocketCustomMessage)));
    }

    private String produceTextMessageFull(WebSocketCustomMessage webSocketCustomMessage) {
        try {
            return objectMapper.writeValueAsString(webSocketCustomMessage);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new WebsocketServiceException("Exception while writing", jsonProcessingException);
        }
    }
}
