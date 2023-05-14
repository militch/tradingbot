package com.github.militch.tradingbot.restapi.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TimeUtil {
    public static LocalDateTime timestampToLocalDateTime(Long t){
        if (t == null) return null;
        Instant i = Instant.ofEpochMilli(t);
        return LocalDateTime.ofInstant(i, ZoneId.systemDefault());
    }

    public static long localDateTimeToTimestamp(LocalDateTime dateTime){
        return dateTime.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
