package com.laundry.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    String originalFileName;
    String url;
}
