package com.example.demo.domain.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmOtpRequest {
    @NotBlank
    String otpId;
    @NotBlank
    String otp;
}
