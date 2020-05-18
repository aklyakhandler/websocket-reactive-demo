package ru.aklyakhandler.websocketdemo.model.external;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockExternal {
    private final Long id;
    private final String name;
    private final Long amount;
    private final BigDecimal price;
    private final BigDecimal totalPrice;
}
