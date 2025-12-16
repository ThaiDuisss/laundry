package com.example.demo.repository;

import com.example.demo.model.entity.Otp;

import java.util.Collection;

public interface RuleState {
    void addTransaction(String accountNumber, String transactionId, Long timestamp);

    Integer countTransactionsInRange(String accountNumber, long from, long to);

    void removeTransactionsBefore(String accountNumber, Long timestamp);

    public <T> Collection<T> getAll(T value, String key, Long start, Long end);

    public Long getLast(String key);

    public Otp getLastOtp(String key);
}

