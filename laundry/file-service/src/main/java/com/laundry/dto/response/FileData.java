package com.laundry.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;
public record FileData(String contentType, Resource resource) {
    public String getContentType() {
        return this.contentType;
    }
    public Resource getResource() {
        return this.resource;
    }
}
