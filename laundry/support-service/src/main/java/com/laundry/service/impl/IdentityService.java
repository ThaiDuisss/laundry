package com.laundry.service.impl;

import com.laundry.dto.DTO.IntrospectToken;
import com.laundry.dto.response.ApiResponse;
import com.laundry.httpClient.AuthClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "IdentityService")
public class IdentityService {
    AuthClient authClient;
    public Long getUserIdFromToken(IntrospectToken token) {
        ResponseEntity<ApiResponse<Object>> resp = authClient.introspect(token);
        if (resp.getBody() != null && resp.getBody().getData() != null) {
            // Trả về userId (chú ý cast nếu data là Number)
            return ((Number) resp.getBody().getData()).longValue();
        }
        return null;
    }
}
