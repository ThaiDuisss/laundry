package com.laundry.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OtpStatus {
    NEW("New"),
    USED("Used"),
    EXPIRED("Expired");
    private final String status;
}
