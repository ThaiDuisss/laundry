package com.laundry.controller;

import com.laundry.dto.response.ApiResponse;
import com.laundry.service.BaseService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class BaseController<T, ID, Req, Res,
        Ser extends BaseService<T, ID, Req, Res>>{
    Ser service;


    @PostMapping
    public ResponseEntity<ApiResponse<Res>> create(@Valid @RequestBody Req request) {
        return ResponseEntity.ok(ApiResponse.<Res>builder()
                .data(service.save(request))
                .code(200)
                .message("Successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Res>> getById(@PathVariable("id") ID id) {
        return ResponseEntity.ok(ApiResponse.<Res>builder()
                .data(service.findById(id))
                .code(200)
                .message("Successfully")
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Res>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<Res>>builder()
                .data(service.findAll())
                .code(200)
                .message("Successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable("id") ID id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.builder().
                message("Successfully")
                .code(200)
                .data("Deleted id: " + id)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Res>> update(@PathVariable("id") ID id, @Valid @RequestBody Req request) {
        return ResponseEntity.ok(ApiResponse.<Res>builder()
                .data(service.update(id, request))
                .code(200)
                .message("Successfully")
                .build());
    }
}
