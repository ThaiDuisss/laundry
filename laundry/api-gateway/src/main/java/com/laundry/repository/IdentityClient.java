package com.laundry.repository;

import com.laundry.dto.IntrospectToken;
import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.ProfileResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange( contentType = MediaType.APPLICATION_JSON_VALUE, value = "/introspect")
    Mono<ApiResponse<Object>> introspect(@RequestBody IntrospectToken token);

    @GetExchange(value = "/get-info/{userId}")
    Mono<ApiResponse<ProfileResponse>> getInfo(@PathVariable("userId") Long userId);
}
