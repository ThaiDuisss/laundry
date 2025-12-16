package com.example.demo.service;
import com.example.demo.model.dto.OtpPaymentResponse;
import com.example.demo.repository.RuleState;
import com.laundry.dto.request.IntrospectPayingToken;
import com.laundry.dto.response.ApiResponse;
import com.laundry.service.impl.RedisService;
import com.laundry.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
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
    @Value("${config.otp.private-key}")
    String privateEncodeKey;

    @Value("${config.otp.public-key}")
    String publicEncodeKey;

    private KeyPair keyPair;

    @Value("${config.otp.header-key-cache}")
    String otpCacheHeaderKey;

    private KeyPair initKeyPair () {
        PrivateKey privateKey = SecurityUtils.readPrivateKey(privateEncodeKey);
        PublicKey publicKey = SecurityUtils.readPublicKey(publicEncodeKey);
        return new KeyPair(publicKey, privateKey);
    }
    boolean userCanResendOtp(Long userId) {
        String key = otpCacheHeaderKey + userId;
        long lastSentTime = ruleState.getLast(key) == null ? 0L : ruleState.getLast(key);
        return Instant.now().toEpochMilli() - lastSentTime >= otpResendIntervalInSeconds * 1000;
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

    public boolean validateConfirmToken(IntrospectPayingToken request) {
        String confirmToken = request.getConfirmToken();
        String expireToken = request.getExpireToken();
        String otpId = request.getOtpId();
        if(StringUtils.isEmpty(confirmToken) || StringUtils.isEmpty(expireToken)){
            return false;
        }
        Long userId = SecurityUtils.getIdFromHeader();
        String signature = userId + "|" + otpId + "|" + expireToken;
        if(keyPair == null){
            keyPair = initKeyPair();
        }
        if(SecurityUtils.verifySignature(signature, confirmToken, keyPair.getPublic())){
            return validateRequest();
        }

        Date tokenExpired= DayUtil.formatDateStringToDate(expireToken, DayUtil.DMY_HMS_SLASH_PATTERN);
        if(tokenExpired.before(new Date())){
            return validateRequest();
        }

        //check OtpId status đã đc dùng chưa

        String key = redisService.normalKey("cart_" + userId.toString());
        Orders order = orderMapper.toOrders(request);
        order.setUserId(userId);
        return true;
    }

}


