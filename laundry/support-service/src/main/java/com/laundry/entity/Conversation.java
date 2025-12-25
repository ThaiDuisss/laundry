package com.laundry.entity;


import com.laundry.enums.ConversationType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Document(collection = "conversation")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
@MongoId
    String id;
    ConversationType type;

    @Indexed(unique = true)
    String participantHash;

    List<ParticipantInfo> participants;

    Instant createDate;

    Instant modifiedDate;
}
