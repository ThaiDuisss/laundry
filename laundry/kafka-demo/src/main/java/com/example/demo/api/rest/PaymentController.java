package com.example.demo.api.rest;

import com.example.demo.domain.dto.TransactionRequest;
import com.example.demo.domain.entity.TransactionEvent;
import com.example.demo.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PaymentController {
    private final TransactionService transactionService;


    @PostMapping
    public TransactionEvent createTransaction(@RequestBody TransactionRequest request) {
        return transactionService.execute(request);
    }

}
