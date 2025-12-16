package com.laundry.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectPayingToken {
    String otpId;
    String confirmToken;
    String expireToken;
}
