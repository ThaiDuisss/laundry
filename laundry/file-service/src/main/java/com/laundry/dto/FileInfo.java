package com.laundry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class FileInfo {
    String name;
    String contentType;
    long size;
    String md5CheckSum;
    String path;
    String url;
}
