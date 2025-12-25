package com.laundry.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
@CompoundIndex(name = "conv_sent_idx", def = "{'conversationId': 1, 'sentAt': -1}")
public class ChatMessage {
    @MongoId
    String id;
    @Indexed
    String conversationId;
    String message;
    ParticipantInfo sender;
    @Indexed
    Instant createDate;
}
