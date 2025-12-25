package com.laundry.dto.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    String channel;
    String recipient;
    String template;
    Map<String, Object> param;
    String subject;
    String body;

}
