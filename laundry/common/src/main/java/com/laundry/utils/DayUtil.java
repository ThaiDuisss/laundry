package com.laundry.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@UtilityClass
public class DayUtil {
    public static final String DMY_HMS_SLASH_PATTERN = "dd/MM/yyyy HH:mm:ss";
    static final String DEFAULT_TIME_ZONE = "UTC+7";
    public static Instant getStartOfDat() {
        ZoneId zoneId = ZoneId.of(DEFAULT_TIME_ZONE);
        LocalDate today = LocalDate.now(zoneId);
        return today.atStartOfDay(zoneId).toInstant();
    }
    public static Instant getCurrentInstant() {
        return ZonedDateTime.now().toInstant();
    }
    public static Date formatDateStringToDate(String dateString, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try{
            return formatter.parse(dateString);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format: " + dateString);
        }
    }
    public static Date parseIsoToDate(String dateString) {
        Instant instant = Instant.parse(dateString); // parse được luôn ISO 8601
        return Date.from(instant); // chuyển về java.util.Date
    }
}
