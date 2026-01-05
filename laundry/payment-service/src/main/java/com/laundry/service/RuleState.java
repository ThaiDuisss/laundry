package com.laundry.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Repository
public class RuleState {
    RedissonClient redissonClient;

    @SneakyThrows
    public void addToRedis(String key, Object value, Long timeStamp) {
        var set = redissonClient.getScoredSortedSet(key);
        if(!set.add(timeStamp, value)){
            throw new Exception("Failed to add to redis");
        }
    }

    public Integer count(String key) {
        var set = redissonClient.getScoredSortedSet(key);
        return set.size();
    }
    public void deleteKey(String key, Long timeStamp) {
        var set  = redissonClient.getScoredSortedSet(key);
        set.removeRangeByScore(0L, true, timeStamp, true);
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
}
