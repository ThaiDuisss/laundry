package com.laundry.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.laundry.entity.WebsocketSession;
import com.laundry.repository.WebSocketRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketService {
    WebSocketRepository webSocketRepository;
    SocketIOServer server;

    public WebsocketSession save(WebsocketSession session) {
        return webSocketRepository.save(session);
    }

    public void deleteBySessionId(String sessionId) {
        webSocketRepository.deleteBySocketSessionId(sessionId);
    }

    public void sendMessageToUser(String email) {
        String socketSessionId = webSocketRepository.findByEmail(email).getSocketSessionId();
        server.getAllClients().forEach(client -> {
            if(client.getSessionId().toString().equals(socketSessionId)) {
                client.sendEvent("valid-email", "successFully");
            }
        });
    }
}
