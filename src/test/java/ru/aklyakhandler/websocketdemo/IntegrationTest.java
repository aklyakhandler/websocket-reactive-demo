package ru.aklyakhandler.websocketdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    int localServerPort;

    @Test
    public void integration() throws Exception {
        WebSocketClient client = new ReactorNettyWebSocketClient();

        URI url = new URI("ws://localhost:" + localServerPort + "/portfolio/1");
        client.execute(url, getWebSocketHandler()).subscribe();

        Thread.sleep(30_000);
    }

    private WebSocketHandler getWebSocketHandler() {
        return session ->
                session.send(
                        Mono.fromCallable(() -> session.textMessage("{\"headers\":{\"MODE\":\"FULL\"}}"))
                )
                        .and(
                                session.receive()
                                        .doOnNext(webSocketMessage -> System.out.println("Client received: " +
                                                webSocketMessage.getPayloadAsText()
                                        ))

                        )
                        .doOnError(throwable -> System.out.println("error" + throwable));

    }
}
