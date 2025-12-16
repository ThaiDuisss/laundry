package com.laundry.dto.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProductInfo {
    private Long productId;
    private String name;
    private Long prices;
    String avatar;
}
