package com.laundry.client;

import com.laundry.dto.request.UserProfile;
import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://localhost:8086")
public interface UserClient {
    @PostMapping( produces = MediaType.APPLICATION_JSON_VALUE, value = "/create-user")
    Object createUser(
            @RequestBody UserProfile request);

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE, value = "/getUserId/{userId}")
    ResponseEntity<ApiResponse<UserProfile>> getByUserId(@PathVariable("userId") Long userId);

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable("userId") Long userId);
}
