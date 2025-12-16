package com.laundry.service;

import com.laundry.dto.IntrospectToken;
import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.repository.IdentityClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class IdentityService {
    IdentityClient identityClient;
    public Mono<ApiResponse<Object>> introspect(String token) {
        return identityClient.introspect(IntrospectToken.builder().token(token).build());
    }
    public Mono<ApiResponse<ProfileResponse>> getInfo(Long userId) {
        return identityClient.getInfo(userId);
    }
}
