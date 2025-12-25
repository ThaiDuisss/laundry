package com.example.demo.service;

import com.example.demo.domain.constant.OtpStatus;
import com.example.demo.domain.dto.OrderRequest;
import com.example.demo.domain.entity.OrderItems;
import com.example.demo.domain.entity.Orders;
import com.example.demo.domain.entity.Otp;
import com.example.demo.domain.mapper.OrderMapper;
import com.example.demo.domain.repository.OrderItemRepository;
import com.example.demo.domain.repository.OrderRepository;
import com.laundry.dto.DTO.ProductInfo;
import com.laundry.dto.request.IntrospectPayingToken;
import com.laundry.dto.response.ApiResponse;
import com.laundry.service.impl.RedisService;
import com.laundry.utils.DayUtil;
import com.laundry.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OrderService {
    final OrderRepository orderRepository;
    final OrderItemRepository orderItemRepository;
    final PayOS payOS;
    final RuleState ruleState;
    final OrderMapper orderMapper;
    final RedisService redisService;

    @Value("${config.otp.private-key}")
    String privateEncodeKey;

    @Value("${config.otp.public-key}")
    String publicEncodeKey;

    @Value("${config.otp.header-key-cache}")
    String otpCacheHeaderKey;

    KeyPair keyPair;


    private KeyPair initKeyPair() {
        PrivateKey privateKey = SecurityUtils.readPrivateKey(privateEncodeKey);
        PublicKey publicKey = SecurityUtils.readPublicKey(publicEncodeKey);
        return new KeyPair(publicKey, privateKey);
    }

    public ApiResponse<CreatePaymentLinkResponse> createPaymentLinkRequestBodyApiResponse(OrderRequest request) {
        String confirmToken = request.getConfirmToken();
        String expireToken = request.getExpireToken();
        String otpId = request.getOtpId();
        if(StringUtils.isEmpty(confirmToken) || StringUtils.isEmpty(expireToken)){

            return ApiResponse.<CreatePaymentLinkResponse>builder()
                    .message("Token is empty")
                    .code(400)
                    .build();
        }
        Long userId = 1L;
        String signature = userId + "|" + otpId + "|" + expireToken;
        if(keyPair == null){
            keyPair = initKeyPair();
        }
        if(!SecurityUtils.verifySignature(signature, confirmToken, keyPair.getPublic())){
            return validateRequest();
        }

        Date tokenExpired =  new Date(Long.parseLong(expireToken));
        if(tokenExpired.before(new Date())){
            return validateRequest();
        }
        //check OtpId status đã đc dùng chưa
        String otpKey = otpCacheHeaderKey + userId;
        Otp otp = ruleState.getLastOtp(otpKey);
        if(otp == null || !otp.getOtpId().equals(otpId) || !OtpStatus.NEW.equals(otp.getStatus())){
            return validateRequest();
        }

        CompletableFuture.runAsync(() -> ruleState.removeTransactionsBefore(otpKey, Instant.now().toEpochMilli()));

        String key = redisService.normalKey("cart_" + userId.toString());
        Orders order = orderMapper.toOrders(request);
        order.setUserId(userId);

// ✅ Lưu order trước để có ID
        List<OrderItems> productInfos = redisService.getListHash(key).stream()
                .map(productInfo -> {
                    OrderItems item = orderMapper.toOrderItems((ProductInfo) productInfo);
                    item.setOrder(order);
                    return item;
                })
                .toList();

        order.setOrderItems(productInfos);
        orderRepository.save(order);
        long amount = order.getTotalPrice().longValue();
        long orderCode = order.getId();

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description("#" + orderCode)
                .returnUrl("https://arboreous-gustavo-metaphorically.ngrok-free.dev/payment-success")
                .cancelUrl("https://arboreous-gustavo-metaphorically.ngrok-free.dev/payment-cancel")
                .build();
        CreatePaymentLinkResponse payResponse = payOS.paymentRequests().create(paymentData);
        redisService.delete(key);
        return ApiResponse.<CreatePaymentLinkResponse>builder()
                .data(payResponse)
                .build();
    }

    private ApiResponse<CreatePaymentLinkResponse> validateRequest() {
        return ApiResponse.<CreatePaymentLinkResponse>builder()
                .code(400)
                .message("Invalid request")
                .build();
    }

    public boolean validateConfirmToken(IntrospectPayingToken request) {
        String confirmToken = request.getConfirmToken();
        String expireToken = request.getExpireToken();
        String otpId = request.getOtpId();
        if(StringUtils.isEmpty(confirmToken) || StringUtils.isEmpty(expireToken)){
            return false;
        }
        Long userId = 1L;
        String signature = userId + "|" + otpId + "|" + expireToken;
        if(keyPair == null){
            keyPair = initKeyPair();
        }
        if(SecurityUtils.verifySignature(signature, confirmToken, keyPair.getPublic())){
            return false;
        }

        Date tokenExpired= DayUtil.formatDateStringToDate(expireToken, DayUtil.DMY_HMS_SLASH_PATTERN);
        if(tokenExpired.before(new Date())){
            return false;
        }

        return true;
    }

}
