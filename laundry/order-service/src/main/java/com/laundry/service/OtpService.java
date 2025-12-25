package com.laundry.service;

import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.OtpPaymentResponse;
import com.laundry.service.impl.RedisService;
import com.laundry.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults( level = lombok.AccessLevel.PRIVATE)
@Service
@Slf4j(topic = "OtpService")
@RequiredArgsConstructor
public class OtpService {
    @Value("${config.otp.length}")
    Integer otpLength;

    @Value("${config.otp.expiration.minutes}")
    Integer otpExpirationInMinutes;

    @Value("${config.otp.validity}")
    Integer otpResendIntervalInSeconds;

    @Value("${config.otp.max-fail-attempts}")
    Integer maxOtpAttempts;

    @Value("${config.otp.header-key-cache}")
    String otpCacheHeaderKey;

    @Value("${config.otp.max-otp-resend-a-day}")
    String maxResend;

    final RedisService redisService;
    final RuleState ruleState;
    boolean userCanResendOtp(Long userId) {
        String key = otpCacheHeaderKey + userId;
        long lastSentTime = Optional.ofNullable(ruleState.getLast(key)).orElse(0L);

        boolean canByTime = Instant.now().toEpochMilli() - lastSentTime >= otpResendIntervalInSeconds * 1000;
        boolean canByCount = ruleState.count(key) <= Integer.parseInt(maxResend);

        return canByTime && canByCount;
    }
    public ApiResponse<OtpPaymentResponse> sendOtp() {
        long userId = SecurityUtils.getIdFromHeader();
        if(!userCanResendOtp(userId)) {
            return ApiResponse.<OtpPaymentResponse>builder()
                    .message("Please wait before requesting a new OTP")
                    .code(404)
                    .build();
        }
        String otpCode = SecurityUtils.createOtp(6);
        return ApiResponse.<OtpPaymentResponse>builder()
                .message("Otp sent successfully")
                .data(OtpPaymentResponse.builder()
                        .otpName(otpCode)
                        .otpId(UUID.randomUUID().toString())
                        .build())
                .build();
    }

    public ApiResponse<OtpPaymentResponse> confirmOtp(String otp) {
        // Implementation for confirming OTP
        return null;
    }

}

