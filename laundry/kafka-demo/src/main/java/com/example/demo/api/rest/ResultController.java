package com.example.demo.api.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ResultController {
    @PostMapping("/payment-success")
    public String paymentSuccess() {
        return "success";
    }
    @PostMapping("/payment-cancel")
    public String paymentCancel() {
        return "cancel";
    }
}
