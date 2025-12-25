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
    date = "2025-10-22T10:54:17+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserProfiles toEntity(UserProfile arg0) {
        if ( arg0 == null ) {
            return null;
        }

        UserProfiles.UserProfilesBuilder userProfiles = UserProfiles.builder();

        userProfiles.userId( arg0.getUserId() );
        userProfiles.fullName( arg0.getFullName() );
        userProfiles.phoneNumber( arg0.getPhoneNumber() );
        userProfiles.avatar( arg0.getAvatar() );

        return userProfiles.build();
    }

    @Override
    public UserResponse toResponse(UserProfiles arg0) {
        if ( arg0 == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.userId( arg0.getUserId() );
        userResponse.fullName( arg0.getFullName() );
        userResponse.phoneNumber( arg0.getPhoneNumber() );
        userResponse.avatar( arg0.getAvatar() );

        return userResponse.build();
    }

    @Override
    public List<UserResponse> toResponseList(List<UserProfiles> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( arg0.size() );
        for ( UserProfiles userProfiles : arg0 ) {
            list.add( toResponse( userProfiles ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(UserProfile arg0, UserProfiles arg1) {
        if ( arg0 == null ) {
            return;
        }

        arg1.setUserId( arg0.getUserId() );
        arg1.setFullName( arg0.getFullName() );
        arg1.setPhoneNumber( arg0.getPhoneNumber() );
        arg1.setAvatar( arg0.getAvatar() );
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
