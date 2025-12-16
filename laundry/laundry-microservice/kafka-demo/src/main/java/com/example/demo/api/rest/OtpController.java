package com.example.demo.api.rest;

import com.example.demo.domain.dto.OtpPaymentResponse;
import com.example.demo.domain.dto.response.ConfirmOtpRequest;
import com.example.demo.service.OtpService;
import com.laundry.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "OTP_CONTROLLER")
@RestController
public class OtpController {
    OtpService otpService;
    @GetMapping("/get-otp")
    public ApiResponse<OtpPaymentResponse> getOtp() {
        return otpService.sendOtp();
    }
    @GetMapping("/confirm-otp")
    public ApiResponse<OtpPaymentResponse> confirmToken(@RequestBody ConfirmOtpRequest confirmOtpRequest) {
        return otpService.confirmOtp(confirmOtpRequest);
    }
}

