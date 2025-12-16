package com.laundry.repository;

import com.laundry.entity.WebsocketSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebSocketSessionRepository extends MongoRepository<WebsocketSession, String> {
    void deleteBySocketSessionId(String sessionId);
    @Query("{ 'userId': { $in: ?0 } }")
    List<WebsocketSession> findAllByUserIdIn(List<Long> userId);
}
