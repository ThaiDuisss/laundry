package com.laundry.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatter {
    static Map<Long, Function<Instant, String>> strategyMap = new LinkedHashMap<>(
    );

    public DateTimeFormatter() {
        strategyMap.put(60L, this::formatInSecond);
        strategyMap.put(3600L, this::formatInMinute);
        strategyMap.put(86400L, this::formatInHour);
        strategyMap.put(604800L, this::formatInDay);
    }

    private String formatInSecond(Instant instant) {
        Long elapsedTime = ChronoUnit.SECONDS.between(instant, Instant.now());
        return elapsedTime + " seconds ago";
    }
    private String formatInMinute(Instant instant) {
        Long elapsedTime = ChronoUnit.MINUTES.between(instant, Instant.now());
        return elapsedTime + " minutes ago";
    }

    private String formatInHour(Instant instant) {
        Long elapsedTime = ChronoUnit.HOURS.between(instant, Instant.now());
        return elapsedTime + " hours ago";
    }

    private String formatInDay(Instant instant) {
        Long elapsedTime = ChronoUnit.DAYS.between(instant, Instant.now());
        return elapsedTime + " days ago";
    }
    public static String formatDateTime(Instant instant) {
        Long elapsedTime = ChronoUnit.SECONDS.between(instant, Instant.now());
        var strategy = strategyMap.entrySet()
                .stream()
                .filter(t ->  elapsedTime < t.getKey()).
                findFirst().orElse(null);
        if(strategy == null) return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .toLocalDate().toString();
        String result = strategy.getValue().apply(instant);
        return result != null ? result : LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .toLocalDate().toString();
    }
}
