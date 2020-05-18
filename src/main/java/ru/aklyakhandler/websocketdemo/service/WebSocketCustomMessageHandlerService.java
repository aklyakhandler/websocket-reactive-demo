package ru.aklyakhandler.websocketdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.util.context.Context;
import ru.aklyakhandler.websocketdemo.model.data.Contract;
import ru.aklyakhandler.websocketdemo.model.data.Stock;
import ru.aklyakhandler.websocketdemo.model.external.StockExternal;
import ru.aklyakhandler.websocketdemo.protocol.WebSocketCustomMessage;
import ru.aklyakhandler.websocketdemo.protocol.WebSocketMessageMode;
import ru.aklyakhandler.websocketdemo.repository.ContractRepository;
import ru.aklyakhandler.websocketdemo.repository.StockRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.aklyakhandler.websocketdemo.model.common.Constant.CLIENT_ID;
import static ru.aklyakhandler.websocketdemo.protocol.WebSocketMessageHeader.MODE;
import static ru.aklyakhandler.websocketdemo.protocol.WebSocketMessageMode.FULL;

@Service
@RequiredArgsConstructor
public class WebSocketCustomMessageHandlerService {
    private final ContractRepository contractRepository;
    private final StockRepository stockRepository;

    public WebSocketCustomMessage apply(WebSocketCustomMessage webSocketCustomMessage, Context context) {
        Long clientId = context.get(CLIENT_ID);
        if (webSocketCustomMessage.getHeaders().containsKey(MODE) &&
                FULL == WebSocketMessageMode.valueOf(webSocketCustomMessage.getHeaders().get(MODE))
        ) {
            List<Contract> contracts = contractRepository.getContractsByUserId(clientId);
            List<Stock> stocks = stockRepository.getStockByIdIn(contracts.stream().map(Contract::getStockId).collect(Collectors.toList()));
            List<StockExternal> externalStocks = merge(contracts, stocks);

            return WebSocketCustomMessage.builder()
                    .headers(Map.of(MODE, FULL.name()))
                    .stocks(externalStocks)
                    .build();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private List<StockExternal> merge(List<Contract> contracts, List<Stock> stocks) {
        return contracts.stream().map(
                contract -> {
                    Stock contractStock = stocks.stream().filter(stock -> stock.getId().equals(contract.getStockId())).findFirst().orElseThrow();
                    return new StockExternal(
                            contractStock.getId(),
                            contractStock.getName(),
                            contract.getStockAmount(),
                            contractStock.getPrice(),
                            contractStock.getPrice()
                                    .multiply(new BigDecimal(contract.getStockAmount()))
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                })
                .collect(Collectors.toList());
    }
}
