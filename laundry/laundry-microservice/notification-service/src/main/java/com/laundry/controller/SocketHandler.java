package com.laundry.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.laundry.entity.WebsocketSession;
import com.laundry.service.WebSocketService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j(topic = "SocketHandler")
@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SocketHandler {
    SocketIOServer server;
    WebSocketService webSocketService;
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String email = client.getHandshakeData().getSingleUrlParam("email");
        String sessionId = client.getSessionId().toString();
        WebsocketSession websocketSession = webSocketService.save(WebsocketSession.builder().socketSessionId(sessionId).email(email).createDate(Instant.now()).build());
        log.info("Client connected: {} and websocketSessionId {}", client.getSessionId(), websocketSession.getId());
    }
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("A client disconnected{}", client.getSessionId());
        webSocketService.deleteBySessionId(client.getSessionId().toString());
    }

    @PostConstruct
    public void startSever() {
        server.addListeners(this);
        server.start();
        log.info("Socket.IO server started");
    }

    @PreDestroy
    public void stopSever() {
        server.stop();
        log.info("Socket.IO server stopped");
    }
}
