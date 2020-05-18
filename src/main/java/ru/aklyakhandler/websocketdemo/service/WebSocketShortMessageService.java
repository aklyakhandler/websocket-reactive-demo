package ru.aklyakhandler.websocketdemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import ru.aklyakhandler.websocketdemo.exception.WebsocketServiceException;
import ru.aklyakhandler.websocketdemo.model.data.Stock;
import ru.aklyakhandler.websocketdemo.protocol.WebSocketCustomMessage;
import ru.aklyakhandler.websocketdemo.repository.ContractRepository;
import ru.aklyakhandler.websocketdemo.repository.StockRepository;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketShortMessageService {
    private final ContractRepository contractRepository;
    private final StockRepository stockRepository;
    private final ObjectMapper objectMapper;

    public Mono<WebSocketMessage> getShortMessage(Long clientId, WebSocketSession webSocketSession) {
        return Mono.fromCallable(() ->
                contractRepository.getStockIdsByUserId(clientId))
                .map(stockRepository::getStockByIdIn)
                .map(stocks -> webSocketSession.textMessage(
                        produceTextMessageShort(stocks)
                ));
    }

    private String produceTextMessageShort(List<Stock> stocks) {
        try {
            return objectMapper.writeValueAsString(
                    produceWebSocketCustomMessageShort(stocks)
            );
        } catch (JsonProcessingException jsonProcessingException) {
            throw new WebsocketServiceException("Exception while writing", jsonProcessingException);
        }
    }

    private WebSocketCustomMessage produceWebSocketCustomMessageShort(List<Stock> stocks) {
        return WebSocketCustomMessage.builder()
                .bodyShort(stocks.stream().collect(
                        HashMap::new,
                        (map, stock) -> map.put(stock.getId(), stock.getPrice()),
                        HashMap::putAll
                ))
//                .headers(Map.of(MODE, SHORT.name()))
                .build();
    }
}
