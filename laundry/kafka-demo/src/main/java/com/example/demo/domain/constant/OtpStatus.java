package com.example.demo.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OtpStatus {
    NEW("New"),
    USED("Used"),
    VERIFY("Verify");
    private final String status;
}
