package com.laundry.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Integer id;
    private String name;
    private Integer categoryId;
    private Map<Integer, BigDecimal> prices;
}
