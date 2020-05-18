package ru.aklyakhandler.websocketdemo.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.aklyakhandler.websocketdemo.model.external.StockExternal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketCustomMessage {
    private Map<WebSocketMessageHeader, String> headers;
    private Map<Long, BigDecimal> bodyShort;
    private List<StockExternal> stocks;
}
