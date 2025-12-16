package com.laundry.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
public class ParticipantInfo {
    Long userId;
    String fullName;
    String avatar;
}
