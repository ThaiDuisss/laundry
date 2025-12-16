package com.example.demo.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum MessageCode {

    RULE_HIT("RULE_HIT");

    private final String code;
}