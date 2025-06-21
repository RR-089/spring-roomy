package com.example.roomy.util;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class DateTimeUtil {
    private static final List<Interval> DAILY_INTERVALS = List.of(
            new Interval(LocalTime.of(1, 30), LocalTime.of(5, 0)),
            new Interval(LocalTime.of(6, 0), LocalTime.of(10, 30))
    );

    public static LocalDateTime calculateExpectedDate(LocalDateTime startDate, long durationInMinutes) {
        long remainingMillis = durationInMinutes * 60 * 1000;
        LocalDateTime current = alignToValidTime(startDate);

        while (remainingMillis > 0) {
            if (isWeekend(current)) {
                current = nextWeekdayStart(current);
                continue;
            }

            boolean timeConsumed = false;

            for (Interval interval : DAILY_INTERVALS) {
                LocalDateTime intervalStart = current.with(interval.start);
                LocalDateTime intervalEnd = current.with(interval.end);

                if (intervalEnd.isBefore(current)) continue;

                if (current.isBefore(intervalStart)) {
                    current = intervalStart;
                }

                Duration available = Duration.between(current, intervalEnd);
                long availableMillis = available.toMillis();

                if (remainingMillis <= availableMillis) {
                    return current.plusNanos(remainingMillis * 1_000_000);
                } else {
                    current = intervalEnd;
                    remainingMillis -= availableMillis;
                    timeConsumed = true;
                }
            }

            if (!timeConsumed) {
                current = nextValidStart(current);
            }
        }

        return current;
    }

    private static boolean isWeekend(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private static LocalDateTime nextWeekdayStart(LocalDateTime dateTime) {
        while (isWeekend(dateTime)) {
            dateTime = dateTime.plusDays(1).with(LocalTime.MIDNIGHT);
        }
        return dateTime.with(DAILY_INTERVALS.get(0).start);
    }

    private static LocalDateTime nextValidStart(LocalDateTime dateTime) {
        LocalDateTime nextDay = dateTime.plusDays(1).with(LocalTime.MIDNIGHT);
        return nextWeekdayStart(nextDay);
    }

    private static LocalDateTime alignToValidTime(LocalDateTime dateTime) {
        if (isWeekend(dateTime)) {
            return nextWeekdayStart(dateTime);
        }

        for (Interval interval : DAILY_INTERVALS) {
            LocalDateTime intervalStart = dateTime.with(interval.start);
            LocalDateTime intervalEnd = dateTime.with(interval.end);

            if (!dateTime.isBefore(intervalStart) && dateTime.isBefore(intervalEnd)) {
                return dateTime;
            }

            if (dateTime.isBefore(intervalStart)) {
                return intervalStart;
            }
        }

        return nextValidStart(dateTime);
    }

    private static class Interval {
        LocalTime start;
        LocalTime end;

        Interval(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }
    }
}
