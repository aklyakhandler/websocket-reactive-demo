package ru.aklyakhandler.websocketdemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.aklyakhandler.websocketdemo.exception.WebsocketServiceException;
import ru.aklyakhandler.websocketdemo.protocol.WebSocketCustomMessage;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

import static ru.aklyakhandler.websocketdemo.model.common.Constant.CLIENT_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandlerService {
    private final static Duration REFRESH_DELAY = Duration.ofSeconds(5);

    private final WebSocketCustomMessageHandlerService webSocketCustomMessageHandlerService;
    private final WebSocketShortMessageService webSocketShortMessageService;
    private final WebSocketFullMessageService webSocketFullMessageService;
    private final ObjectMapper objectMapper;

    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> intervalSendMono = Mono.subscriberContext()
                .map(context -> (Long) context.get(CLIENT_ID))
                .flatMap(clientId -> session.send(
                        Flux.interval(REFRESH_DELAY)
                                .flatMap(ignored -> webSocketShortMessageService.getShortMessage(clientId, session))
                        )
                )
                .doOnError(throwable -> log.error("error", throwable));

        Mono<Void> processClientRequestMono = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::parse)
                .doOnNext(webSocketCustomMessage -> log.info("Received object {}", webSocketCustomMessage))
                .flatMap(requestMessage ->
                        Mono.subscriberContext()
                                .map(context ->
                                        webSocketCustomMessageHandlerService.apply(requestMessage, context))
                )
                .flatMap(webSocketCustomMessage -> webSocketFullMessageService.getFullMessage(webSocketCustomMessage, session))
                .flatMap(webSocketMessage -> session.send(Mono.fromCallable(() -> webSocketMessage)))
                .then();
        return Mono.zip(processClientRequestMono, intervalSendMono)
                .subscriberContext(context -> {
                    log.info("Subscribed with id {}", getClientIdFromWebSocketSession(session));
                    return context.put(CLIENT_ID, getClientIdFromWebSocketSession(session));
                }).then()
                .doOnError(throwable -> log.error("error", throwable));
    }


    private Long getClientIdFromWebSocketSession(WebSocketSession webSocketSession) {
        URI uri = webSocketSession.getHandshakeInfo().getUri();
        UriTemplate template = new UriTemplate("/portfolio/{clientId}");
        Map<String, String> parameters = template.match(uri.getPath());
        return Long.parseLong(parameters.get("clientId"));
    }

    private WebSocketCustomMessage parse(String payload) {
        try {
            return objectMapper.readValue(payload, WebSocketCustomMessage.class);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new WebsocketServiceException("Exception while parsing", jsonProcessingException);
        }
    }


}
