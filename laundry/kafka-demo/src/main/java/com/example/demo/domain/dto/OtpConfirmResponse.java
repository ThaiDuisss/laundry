package com.example.demo.domain.dto;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpConfirmResponse {
    String otpId;
    String confirmToken;
    String expirationTime;
}
