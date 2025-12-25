package com.example.demo.infrastructor.repository;

import com.example.demo.model.entity.Otp;
import com.example.demo.repository.RuleState;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Repository
@Slf4j(topic = "COUNT")
public class RuleStateImpl implements RuleState {
    RedissonClient redissonClient;

    @SneakyThrows
    @Override
    public void addTransaction(String accountNumber, String transactionId, Long timestamp) {
        var transaction = redissonClient.getScoredSortedSet(accountNumber);
        if (transaction.add(timestamp, transactionId)) {
            throw new Exception("Failed to add transaction");
        }
    }

    @Override
    public Integer countTransactionsInRange(String accountNumber, long from, long to) {
        log.info("long{} + to {}", from, to);
        var transaction = redissonClient.getScoredSortedSet(accountNumber);
        return transaction.count(from, true, to, true);
        
    }

    @Override
    public void removeTransactionsBefore(String accountNumber, Long timestamp) {
        var transaction = redissonClient.getScoredSortedSet(accountNumber);
        transaction.removeRangeByScore(0L, true, timestamp, true);
    }

    public <T> Collection<T> getAll(T value,String key, Long start, Long end) {
        return Optional.ofNullable(redissonClient.getScoredSortedSet(key).valueRange(start, true, end, true))
                .orElse(Collections.emptyList())
                .stream()
                .map(obj -> (T) obj)
                .toList();
    }
    public Long getLast(String key) {
        return (Long)redissonClient.getScoredSortedSet(key).last();
    }

    public Otp getLastOtp( String key) {
        return (Otp)redissonClient.getScoredSortedSet(key).last();
    }
}