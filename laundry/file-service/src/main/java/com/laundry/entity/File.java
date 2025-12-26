package com.laundry.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.lang.annotation.Documented;

@Document(collection = "file-mgmt")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @MongoId
    String id;
    String url;
    Long ownerId;
    String path;
    Long size;
    String contentType;
    String md5CheckSum;

    public File(long l, String file, String contentType, long l1) {
    }
}
