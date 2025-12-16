package com.laundry.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.laundry.dto.DTO.IntrospectToken;
import com.laundry.entity.WebsocketSession;
import com.laundry.service.WebSocketSessionService;
import com.laundry.service.impl.IdentityService;
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
    IdentityService identityService;
    WebSocketSessionService webSocketSessionService;
    @OnConnect
    public void clientConnected(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        String sessionId = client.getSessionId().toString();
        log.info("Client connected: {} and token {}", client.getSessionId(), token);
        IntrospectToken  introspectToken = IntrospectToken.builder().token(token).build();
        Long userId = identityService.getUserIdFromToken(introspectToken);
        if(userId == null) {
            log.info("Client disconnected due to invalid token: {}", client.getSessionId());
            client.disconnect();
        }
        WebsocketSession websocketSession = webSocketSessionService.save(WebsocketSession.builder().socketSessionId(sessionId).userId(userId).createDate(Instant.now()).build());
        log.info("Client connected: {} and token {} and websocketSessionId {}", client.getSessionId(), token, websocketSession.getId());
    }
    @OnDisconnect
    public void disconnect(SocketIOClient client) {
        log.info("A client disconnected{}", client.getSessionId());
        webSocketSessionService.deleteBySessionId(client.getSessionId().toString());
    }
    @PostConstruct
    public void startServer() {
        server.addListeners(this);
        server.start();
        log.info("Socket.IO server started");
    }
    @PreDestroy
    public void stopServer() {
        server.stop();
        log.info("Socket.IO server stopped");
    }
}
