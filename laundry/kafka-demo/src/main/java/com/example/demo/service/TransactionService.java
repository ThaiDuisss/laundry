package com.example.demo.service;


import com.example.demo.domain.dto.TransactionRequest;
import com.example.demo.domain.entity.TransactionEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)

public class TransactionService {
    final TransactionProducer transactionProducer;


    public TransactionEvent execute(TransactionRequest request) {
        var transactionId = UUID.randomUUID().toString();
        var transactionEvent = TransactionEvent.builder()
                .transactionId(transactionId)
                .transactionType(request.getTransactionType())
                .debitAccount(request.getDebitAccount())
                .creditAccount(request.getCreditAccount())
                .amount(request.getAmount())
                .status(1)
                .createdAt(System.currentTimeMillis())
                .build();
        transactionProducer.send(transactionEvent);
        return transactionEvent;
    }
}
