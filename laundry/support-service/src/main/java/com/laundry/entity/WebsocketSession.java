package com.laundry.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Document(collection = "websocketSession")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketSession {
    @MongoId
    String id;

    String socketSessionId;

    Long userId;

    Instant createDate;
}
