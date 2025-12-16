package com.laundry.httpClient;

import com.laundry.dto.DTO.IntrospectToken;
import com.laundry.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:8081")
public interface AuthClient {
    @PostMapping(value = "/introspect", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Object>> introspect(@RequestBody IntrospectToken token);
}
