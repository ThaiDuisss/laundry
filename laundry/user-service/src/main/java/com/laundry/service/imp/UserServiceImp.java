package com.laundry.service.imp;

import com.laundry.Client.FileClient;
import com.laundry.dto.request.UserProfile;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserProfiles;
import com.laundry.enums.ErrorEnum;
import com.laundry.exception.AppException;
import com.laundry.mapper.UserMapper;
import com.laundry.repository.UserRepository;
import com.laundry.service.UserService;
import com.laundry.service.impl.BaseServiceImp;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp extends BaseServiceImp<UserProfiles, Long, UserProfile, UserResponse, UserMapper, UserRepository> implements UserService {
FileClient fileClient;
    public UserServiceImp(UserMapper mapper, UserRepository repository, FileClient fileClient) {
        super(mapper, repository);
        this.fileClient = fileClient;
    }
    @Override
    protected ErrorEnum getNotFoundError() {
        return ErrorEnum.USER_NOT_FOUND;
    }

    @Override
    public void delete(Long id) {
        UserProfiles entity = repository.findByUserId(id)
                .orElseThrow(() -> new AppException(getNotFoundError()));
        repository.delete(entity);
    }



    @Override
    public UserProfile getByUserId(Long userId) {
        return mapper.toUserProfile(repository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)));
    }

    @Override
    public UserProfile updateAvatar(MultipartFile file, Long userId) {
        UserProfiles entity = repository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        var response = fileClient.uploadFile(file);
        entity.setAvatar(response.getBody().getData().getUrl());
        return mapper.toUserProfile(repository.save(entity));
    }

    @Override
    public List<UserResponse> getByFullName(String key) {
        return mapper.toResponseList(repository.findByFullName(key));
    }
}
