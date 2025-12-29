package com.laundry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmOtpRequest {
    @NotBlank
    String otpId;
    @NotBlank
    String otp;
}
