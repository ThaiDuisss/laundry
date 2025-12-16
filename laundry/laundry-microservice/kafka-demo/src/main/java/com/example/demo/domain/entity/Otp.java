package com.example.demo.domain.entity;

import com.example.demo.domain.constant.OtpStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.laundry.entity.AbstractEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Otp implements Serializable {
    String otpId;

    String otpCode;

//    int attemptFail;

    OtpStatus status;

    Long userId;

//    String cartId;

    Instant createdAt = Instant.now();
}
