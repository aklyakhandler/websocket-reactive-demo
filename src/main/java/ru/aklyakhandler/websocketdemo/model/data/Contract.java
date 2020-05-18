package ru.aklyakhandler.websocketdemo.model.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Table("contract")
public class Contract {
    private final Long id;
    private final Long userId;
    private final Long stockId;
    private Long stockAmount;

    public static Contract of(Long userId, Long stockId, Long stockAmount) {
        return new Contract(null, userId, stockId, stockAmount);
    }

    public Contract withId(Long id) {
        return new Contract(id, this.userId, this.stockId, this.stockAmount);
    }
}
