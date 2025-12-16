package com.example.demo.controller;

import com.laundry.dto.request.IntrospectPayingToken;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OtpController {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/verify-confirmToken")
    boolean checkConfirmToken(@RequestBody IntrospectPayingToken request) {
        return true;
    }
}
