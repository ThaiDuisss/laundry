package com.laundry.service;

import com.laundry.dto.request.UserProfile;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends BaseService<UserProfiles, Long, UserProfile, UserResponse> {
    UserProfile getByUserId(Long userId);
    UserProfile updateAvatar(MultipartFile file, Long userId);
    List<UserResponse> getByFullName(String key);

}
