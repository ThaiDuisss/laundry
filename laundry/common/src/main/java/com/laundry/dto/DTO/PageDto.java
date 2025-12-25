package com.laundry.dto.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    Integer category;
    String keyword;
}
