package com.example.demo.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MessageCode {

    FUND_TRANSFER("FUND_TRANSFER"),

    SEND_EMAIL("Send otp email");

    private final String code;
}
