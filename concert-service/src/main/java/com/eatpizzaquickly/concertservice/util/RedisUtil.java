package com.eatpizzaquickly.concertservice.util;

public class RedisUtil {

    public static String getAvailableSeatsKey(Long concertId) {
        return "concert:%s:available_seats".formatted(concertId);
    }

    public static String getViewCountKey() {
        return "concert:view_count";
    }

    public static String getQueueKey(Long concertId) {
        return "concert:%s:queue".formatted(concertId);
    }

    public static String getInReservationKey(Long concertId) {
        return "concert:%s:in_reservation".formatted(concertId);
    }

    public static String getRequestCountKey(Long concertId) {
        return "concert:%s:requestCount".formatted(concertId);
    }
}
