package com.laundry.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Otp extends AbstractEntity<Long>{
    String otpCode;

    String attemptFail;

    String status;

    String userId;
}
