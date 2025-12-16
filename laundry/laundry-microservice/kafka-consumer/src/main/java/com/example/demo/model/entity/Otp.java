package com.example.demo.model.entity;

import com.laundry.entity.AbstractEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Otp extends AbstractEntity<Long> {
    String otpCode;

    String attemptFail;

    String status;

    String userId;
}
