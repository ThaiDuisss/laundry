package com.laundry.dto.response;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpPaymentResponse {
    String otpId;
    String otpName;
}
