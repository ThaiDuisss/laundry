package com.laundry.repository;

import com.laundry.dto.FileInfo;
import com.laundry.entity.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Repository
public class FileRepository {
    @Value("${app.file.upload-dir}")
    private String uploadDir;
    @Value("${app.file.upload-url}")
    private String uploadUrl;

    public FileInfo store(MultipartFile file) throws IOException {
        Path folder = Paths.get(uploadDir);
        // Tạo tên file theo UUID
        //Nếu không có extension thì chỉ lưu mỗi tên UUID
        String fileExtension = StringUtils.getFilenameExtension(
                file.getOriginalFilename());
        String fileName = Objects.isNull(fileExtension) ?
                UUID.randomUUID().toString() :
                UUID.randomUUID().toString() + "."
                        + fileExtension;
        //tạo đường dẫn file an toàn và tuyệt đối
        Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return FileInfo.builder().
                name(fileName)
                .size(file.getSize())
                .contentType(file.getContentType())
                .md5CheckSum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                .path(filePath.toString())
                .url(uploadUrl + fileName).build();
    }

    public Resource read(File file) throws IOException {
        var data = Files.readAllBytes(Path.of(file.getPath()));
        return new ByteArrayResource(data);
    }
}
