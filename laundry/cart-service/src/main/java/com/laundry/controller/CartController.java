package com.laundry.controller;

import com.laundry.dto.DTO.ProductInfo;
import com.laundry.dto.response.ApiResponse;
import com.laundry.service.CartService;
import com.laundry.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "CartController")
public class CartController {

    CartService cartService;
    static String GUEST_CARD_ID = "GUEST_ID_";

    /**
     * Lấy giỏ hàng (guest hoặc user)
     */
    @GetMapping("/get-cart")
    public ResponseEntity<ApiResponse<?>> getCart(
            @CookieValue(value = "guest", required = false) String guestCookie) {

        List<ProductInfo> cart = cartService.getCart(guestCookie);

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
        Long userId = SecurityUtils.getIdFromHeader();

        // Nếu là user và có guestCookie -> xóa cookie sau khi merge
        if (userId != null && guestCookie != null) {
            ResponseCookie deleteGuestCookie = ResponseCookie.from("guest", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("None")
                    .maxAge(0)
                    .build();
            responseBuilder.header(HttpHeaders.SET_COOKIE, deleteGuestCookie.toString());
        }

        return responseBuilder.body(
                ApiResponse.builder()
                        .status("Get cart successfully")
                        .code(200)
                        .data(cart)
                        .build()
        );
    }

    /**
     * Thêm sản phẩm vào giỏ (guest hoặc user)
     */
    @PostMapping("/add-cart")
    public ResponseEntity<ApiResponse<?>> addCart(
            @RequestBody ProductInfo productInfo,
            @CookieValue(value = "guest", required = false) String guestCookie) {

        Long userId = SecurityUtils.getIdFromHeader();
        log.info("GuestCookie {}, UserId {}", guestCookie, userId);

        String guestId = (guestCookie != null) ? guestCookie : createGuestCartId();

        if (userId != null) {
            return resultForUser(cartService.saveProductInfo(productInfo, userId));
        }
        return resultForGuest(cartService.addCartToRedis(null, guestId, productInfo), guestId);
    }

    /**
     * Tạo mã giỏ cho guest
     */
    private String createGuestCartId() {
        return GUEST_CARD_ID + UUID.randomUUID();
    }

    private ResponseEntity<ApiResponse<?>> resultForGuest(boolean result, String guestID) {
        ResponseCookie guestCookie = ResponseCookie.from("guest", guestID)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(60 * 60 * 24 * 7L) // 7 ngày
                .build();

        ApiResponse.ApiResponseBuilder<Object> response = ApiResponse.builder().code(result ? 200 : 201)
                .status(result ? "Add to cart successfully" : "Product existed in cart");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, guestCookie.toString())
                .body(response.build());
    }

    private ResponseEntity<ApiResponse<?>> resultForUser(boolean result) {
        ApiResponse.ApiResponseBuilder<Object> response = ApiResponse.builder().code(result ? 200 : 201)
                .status(result ? "Add to cart successfully" : "Product existed in cart");
        return ResponseEntity.ok(response.build());
    }
}
