package com.laundry.httpClient;

import com.laundry.dto.request.UserProfile;
import com.laundry.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8086")
public interface ProfileClient {
    @GetMapping(value = "getUserId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiResponse<UserProfile>> getById(@PathVariable("userId") Long id);
}
