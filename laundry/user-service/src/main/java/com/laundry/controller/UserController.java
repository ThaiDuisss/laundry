package com.laundry.controller;

import com.laundry.dto.request.UserProfile;
import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.FileResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserProfiles;
import com.laundry.service.UserService;
import com.laundry.utils.SecurityUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class UserController extends BaseController<UserProfiles, Long, UserProfile, UserResponse, UserService> {

    public UserController(UserService service) {
        super(service);
    }

    @Override
    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody UserProfile request) {
        return super.create(request);
    }

    @GetMapping("/getUserId/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.<UserProfile>builder()
                .data(service.getByUserId(userId))
                .message("User fetched successfully")
                .build());
    }

    @PutMapping( value = "/avatar")
    public ResponseEntity<ApiResponse<UserProfile>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long userId = SecurityUtils.getIdFromHeader();
        return ResponseEntity.ok(ApiResponse.<UserProfile>builder()
                .data(service.updateAvatar(file, userId))
                .message("User fetched successfully")
                .build());
    }

    @GetMapping("/search/{key}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getByKey(@PathVariable("key") String key) {
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .data(service.getByFullName(key))
                .message("User fetched successfully")
                .build());
    }

}
