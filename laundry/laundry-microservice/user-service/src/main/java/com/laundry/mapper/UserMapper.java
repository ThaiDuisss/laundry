package com.laundry.mapper;

import com.laundry.dto.request.UserProfile;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserProfiles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserProfiles, UserProfile, UserResponse> {
    UserProfile toUserProfile(UserProfiles entity);
}
