package com.laundry.service;

import com.laundry.dto.response.FileData;
import com.laundry.dto.response.FileResponse;
import com.laundry.entity.File;
import com.laundry.enums.ErrorEnum;
import com.laundry.exception.AppException;
import com.laundry.mapper.FileMapper;
import com.laundry.repository.FileMgmtRepository;
import com.laundry.repository.FileRepository;
import com.laundry.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class FileService {
    FileMgmtRepository fileMgmtRepository;
    FileRepository fileRepository;
    FileMapper fileMapper;
    public FileResponse uploadFile(MultipartFile file) throws IOException {
        Long userId = SecurityUtils.getIdFromHeader();
        //lưu vào local
        var info = fileRepository.store(file);
        File fileEntity = fileMapper.toFile(info);
        fileEntity.setOwnerId(userId);
        fileMgmtRepository.save(fileEntity);
        return FileResponse.builder()
                .originalFileName(file.getOriginalFilename())
                .url(info.getUrl())
                .build();
    }

    public FileData downloadFile(String fileName) throws IOException {
        var fileMgmt =fileMgmtRepository.findById(fileName).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        var resource = fileRepository.read(fileMgmt);
        return new FileData(fileMgmt.getContentType(), resource);
    }
}
