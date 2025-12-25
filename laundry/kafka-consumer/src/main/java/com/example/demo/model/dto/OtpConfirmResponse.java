package com.example.demo.model.dto;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
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
