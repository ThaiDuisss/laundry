package com.example.demo.service;

import com.example.demo.domain.entity.Otp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Repository
@Slf4j(topic = "COUNT")
public class RuleState  {
    RedissonClient redissonClient;

    @SneakyThrows
    public void addVariable(String key, Otp otp, Long timestamp) {
        var transaction = redissonClient.getScoredSortedSet(key);
        if (!transaction.add(timestamp, otp)) {
            throw new Exception("Failed to add transaction");
        }
    }

    public Integer count(String accountNumber) {
        var transaction = redissonClient.getScoredSortedSet(accountNumber);
        return transaction.size();
        
    }

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
        RScoredSortedSet<Otp> set = redissonClient.getScoredSortedSet(key);
        return set.isEmpty() ? null : set.lastScore().longValue();
    }

    public Otp getLastOtp( String key) {
        RScoredSortedSet<Otp> set = redissonClient.getScoredSortedSet(key);
        return set.isEmpty() ? null : set.last();
    }
}