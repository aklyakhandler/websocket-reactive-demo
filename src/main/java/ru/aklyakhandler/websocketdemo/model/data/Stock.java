package ru.aklyakhandler.websocketdemo.model.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Table("stock")
public class Stock {
    @Id
    private final Long id;
    private final String name;
    private BigDecimal price;

    public static Stock of(String name, BigDecimal price) {
        return new Stock(null, name, price);
    }

    public Stock withId(Long id) {
        return new Stock(id, this.name, this.price);
    }
}
