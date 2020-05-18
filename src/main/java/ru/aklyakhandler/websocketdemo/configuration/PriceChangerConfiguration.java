package ru.aklyakhandler.websocketdemo.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.aklyakhandler.websocketdemo.model.data.Stock;
import ru.aklyakhandler.websocketdemo.repository.StockRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class PriceChangerConfiguration {

    private final static SecureRandom SECURE_RANDOM = new SecureRandom(new byte[]{0});
    private final StockRepository stockRepository;

    @Bean(destroyMethod = "dispose")
    public Disposable changeStockPrice() {
        return changeStockPriceMono().subscribe();
    }

    private Mono<Void> changeStockPriceMono() {
        return Flux.interval(Duration.ofSeconds(7))
                .map(ignored -> changePrices(stockRepository.findAll()))
                .then()
                .onErrorResume(throwable -> {
                    log.error("error in stock price change:", throwable);
                    return changeStockPriceMono();
                })
                ;
    }

    private Object changePrices(Iterable<Stock> stocks) {
        for (Stock stock : stocks) {
            stock.setPrice(generateNewPrice(stock.getPrice()));
        }
        log.debug("Changed prices: {}", stocks);
        return stockRepository.saveAll(stocks);
    }

    private BigDecimal generateNewPrice(BigDecimal currentPrice) {
        if (SECURE_RANDOM.nextBoolean()) {
            return currentPrice.multiply(BigDecimal.valueOf(1.05)).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
        } else {
            return currentPrice.multiply(BigDecimal.valueOf(0.95)).setScale(4, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    }
}
