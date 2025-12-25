package com.example.demo.service;

import com.example.demo.domain.constant.OtpStatus;
import com.example.demo.domain.dto.OtpPaymentResponse;

import com.example.demo.domain.dto.response.ConfirmOtpRequest;
import com.example.demo.domain.entity.Otp;
import com.laundry.dto.response.ApiResponse;
import com.laundry.utils.DayUtil;
import com.laundry.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Service
@Slf4j(topic = "OtpService")
@RequiredArgsConstructor
public class OtpService {
    final RuleState ruleState;

    @Value("${config.otp.length}")
    int otpLength;

    @Value("${config.otp.expiration-minutes}")
    int otpExpirationInMinutes;

    @Value("${config.otp.validity}")
    int otpResendIntervalInSeconds;

    @Value("${config.otp.max-fail-attempts}")
    int maxOtpAttempts;

    @Value("${config.otp.header-key-cache}")
    String otpCacheHeaderKey;

    @Value("${config.otp.max-otp-resend-a-day}")
    int maxResend;

    @Value("${config.otp.private-key}")
    String privateEncodeKey;

    @Value("${config.otp.public-key}")
    String publicEncodeKey;

    KeyPair keyPair;

    private KeyPair initKeyPair() {
        PrivateKey privateKey = SecurityUtils.readPrivateKey(privateEncodeKey);
        PublicKey publicKey = SecurityUtils.readPublicKey(publicEncodeKey);
        return new KeyPair(publicKey, privateKey);
    }

    boolean userCanResendOtp(String key) {

        long lastSentTime = Optional.ofNullable(ruleState.getLast(key)).orElse(0L);

        boolean canByTime = Instant.now().toEpochMilli() - lastSentTime >= otpResendIntervalInSeconds * 1000;
        boolean canByCount = ruleState.count(key) <= maxResend;

        return canByTime && canByCount;
    }

    public ApiResponse<OtpPaymentResponse> sendOtp() {
        Long userId = 1L;
        String key = otpCacheHeaderKey + userId;
        if (!userCanResendOtp(key)) {
            return ApiResponse.<OtpPaymentResponse>builder()
                    .message("Please wait before requesting a new OTP")
                    .code(404)
                    .build();
        }
        String otpCode = SecurityUtils.createOtp(otpLength);
        String otpId = UUID.randomUUID().toString();
        Otp otp = Otp.builder()
                .userId(userId)
                .otpCode(otpCode)
                .status(OtpStatus.NEW)
//                .attemptFail(0)
                .createdAt(Instant.now())
                .otpId(otpId)
                .build();

        ruleState.addVariable(key, otp, Instant.now().toEpochMilli());

        return ApiResponse.<OtpPaymentResponse>builder()
                .message("Otp sent successfully")
                .data(OtpPaymentResponse.builder()
                        .otpId(otpId)
                        .otp(otpCode)
                        .build())
                .build();
    }

    public ApiResponse<OtpPaymentResponse> confirmOtp(ConfirmOtpRequest request) {
        Long userId = 1L;
        String otpId = request.getOtpId();
        String otpCode = request.getOtp();
        Otp otp = ruleState.getLastOtp(otpCacheHeaderKey + userId);
        String expireToken = Long.toString(Instant.now().toEpochMilli() + (10 * 60 * 1000));
        if(!otp.getOtpCode().equals(otpCode) || !otp.getOtpId().equals(otpId)) {
            return ApiResponse.<OtpPaymentResponse>builder()
                    .message("Otp not exact!!")
                    .code(400)
                    .status("FAIL")
                    .build();
        }
        Date otpCreate= DayUtil.parseIsoToDate(otp.getCreatedAt().toString());
        if(otpCreate.after(new Date())) {
            return ApiResponse.<OtpPaymentResponse>builder()
                    .message("Otp is expired!!")
                    .code(400)
                    .status("FAIL")
                    .build();
        }
        String confirmTokenData = userId + "|" + otpId + "|" + expireToken;
        if (keyPair == null) {
            keyPair = initKeyPair();
        }
        String confirmToken = SecurityUtils.signData(confirmTokenData, keyPair.getPrivate());

        return ApiResponse.<OtpPaymentResponse>builder()
                .message("Otp confirmed successfully")
                .data(OtpPaymentResponse.builder()
                        .confirmToken(confirmToken)
                        .expireToken(expireToken)
                        .build())
                .build();
    }

}


