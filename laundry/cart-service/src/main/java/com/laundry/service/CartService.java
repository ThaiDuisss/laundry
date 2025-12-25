package com.laundry.service;

import com.laundry.dto.DTO.ProductInfo;
import com.laundry.service.impl.RedisService;
import com.laundry.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
@Slf4j(topic = "CartService")
public class CartService {

    RedisService redisService;
    String HEAD_KEY_CART = "cart_";

    public boolean addCartToRedis(Long userId, String guestId, ProductInfo productInfo) {
        String field = productInfo.getProductId() + "_" + productInfo.getPrices();
        String key = (userId == null) ? guestId : HEAD_KEY_CART + userId.toString() ;

        log.info("Field {}, ProductInfo {}", field, productInfo);

        if (!redisService.hashExist(key, field)) {
            redisService.hashSet(key, field, productInfo);
            return true;
        }
        return false;
    }

    public boolean  saveProductInfo(ProductInfo productInfo, Long userId) {
        log.info("UserId {}, ProductInfo {}", userId, productInfo);

        if (userId == null) return false; // user chưa đăng nhập

        return addCartToRedis(userId, null, productInfo);
    }

    public List<ProductInfo> getCart(String guestId) {

        Long userId = SecurityUtils.getIdFromHeader();
        String userKey = (userId != null) ? HEAD_KEY_CART + userId : null;

        log.info("GuestId {}, UserId {}", guestId, userId);

        // Nếu chưa login → lấy cart guest
        if (userId == null) {
            return redisService.getListHash(guestId).stream()
                    .map(o -> (ProductInfo) o).toList();
        }

        // Nếu đã login → merge guest cart (nếu có)
        if (guestId != null && redisService.exists(guestId)) {
            Map<String, Object> guestCart = redisService.getField(guestId);
            for (Map.Entry<String, Object> entry : guestCart.entrySet()) {
                String field = entry.getKey();
                ProductInfo product = (ProductInfo) entry.getValue();

                if (!redisService.hashExist(userKey, field)) {
                    redisService.hashSet(userKey, field, product);
                }
            }

            // Xóa giỏ guest async
            CompletableFuture.runAsync(() -> redisService.delete(guestId));
        }

        // Trả về giỏ user
        return redisService.getListHash(userKey).stream()
                .map(o -> (ProductInfo) o).toList();
    }
}
