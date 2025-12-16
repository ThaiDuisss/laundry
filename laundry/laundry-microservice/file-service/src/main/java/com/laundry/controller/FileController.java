package com.laundry.controller;

import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.FileResponse;
import com.laundry.service.FileService;
import com.laundry.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileController {
    FileService fileService;

    @PostMapping(value = "media/upload")
    ResponseEntity<ApiResponse<FileResponse>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(ApiResponse.<FileResponse>builder()
                .code(200)
                .message("Success")
                .data(fileService.uploadFile(file))
                .build());
    }

    @GetMapping(value = "media/download/{fileName}")
    ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) throws IOException {
        var resource = fileService.downloadFile(fileName);
        return ResponseEntity.<Resource>ok()
                .header(HttpHeaders.CONTENT_TYPE, resource.contentType())
                .body(resource.resource());
    }
}
