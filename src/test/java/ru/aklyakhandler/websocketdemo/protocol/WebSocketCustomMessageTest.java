package ru.aklyakhandler.websocketdemo.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class WebSocketCustomMessageTest {

    @Test
    public void deserealizationTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = "{\"headers\":{\"MODE\":\"FULL\"}}";
        objectMapper.readValue(payload, WebSocketCustomMessage.class);
    }

}