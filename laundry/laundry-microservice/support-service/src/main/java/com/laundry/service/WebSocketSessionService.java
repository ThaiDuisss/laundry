package com.laundry.service;

import com.laundry.entity.WebsocketSession;
import com.laundry.repository.WebSocketSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketSessionService {
    WebSocketSessionRepository webSocketSessionRepository;

    public WebsocketSession save(WebsocketSession session) {
        return webSocketSessionRepository.save(session);
    }

    public void deleteBySessionId(String sessionId) {
        webSocketSessionRepository.deleteBySocketSessionId(sessionId);
    }
}
