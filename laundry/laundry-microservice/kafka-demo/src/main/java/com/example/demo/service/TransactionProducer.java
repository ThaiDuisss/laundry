package com.example.demo.service;

import com.example.demo.domain.entity.TransactionEvent;
import org.springframework.stereotype.Component;

@Component
public interface TransactionProducer {
    void send(TransactionEvent transactionEvent);
}
