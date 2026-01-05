package com.laundry.mapper;

import com.laundry.dto.request.UserProfile;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserProfiles;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-03T15:41:33+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserProfiles toEntity(UserProfile request) {
        if ( request == null ) {
            return null;
        }

        UserProfiles.UserProfilesBuilder userProfiles = UserProfiles.builder();

        userProfiles.userId( request.getUserId() );
        userProfiles.fullName( request.getFullName() );
        userProfiles.phoneNumber( request.getPhoneNumber() );
        userProfiles.avatar( request.getAvatar() );

        return userProfiles.build();
    }

    @Override
    public UserResponse toResponse(UserProfiles entity) {
        if ( entity == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.userId( entity.getUserId() );
        userResponse.fullName( entity.getFullName() );
        userResponse.phoneNumber( entity.getPhoneNumber() );
        userResponse.avatar( entity.getAvatar() );

        return userResponse.build();
    }

    @Override
    public List<UserResponse> toResponseList(List<UserProfiles> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( entities.size() );
        for ( UserProfiles userProfiles : entities ) {
            list.add( toResponse( userProfiles ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(UserProfile request, UserProfiles entity) {
        if ( request == null ) {
            return;
        }

        entity.setUserId( request.getUserId() );
        entity.setFullName( request.getFullName() );
        entity.setPhoneNumber( request.getPhoneNumber() );
        entity.setAvatar( request.getAvatar() );
    }

    @Override
    public UserProfile toUserProfile(UserProfiles entity) {
        if ( entity == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setUserId( entity.getUserId() );
        userProfile.setFullName( entity.getFullName() );
        userProfile.setPhoneNumber( entity.getPhoneNumber() );
        userProfile.setAvatar( entity.getAvatar() );

        return userProfile;
    }
}
