package com.example.roomy.util;

import com.example.roomy.dto.calendar.HolidayDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.*;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DateTimeUtil {
    private final List<Interval> DAILY_INTERVALS = List.of(
            new Interval(LocalTime.of(1, 30), LocalTime.of(5, 0)),
            new Interval(LocalTime.of(6, 0), LocalTime.of(10, 30))
    );

    private final CalendarUtil calendarUtil;

    public LocalDateTime calculateExpectedDate(LocalDateTime startDate, long durationInMinutes) {
        log.info("Calculating expected date...");

        LocalDate currentDate = LocalDate.now();
        LocalDate lastDateOfYear = Year.now().atDay(Year.now().length());

        long remainingMillis = durationInMinutes * 60 * 1000;
        LocalDateTime current = alignToValidTime(startDate);

        Mono<List<HolidayDTO>> holidayDates =
                this.calendarUtil.getHolidays(currentDate, lastDateOfYear);

        while (remainingMillis > 0) {

            if (isHoliday(LocalDate.from(current), holidayDates)) {
                current = nextValidStart(current);
                continue;
            }

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

    private boolean isWeekend(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private boolean isHoliday(LocalDate date, Mono<List<HolidayDTO>> holidayMono) {
        return Boolean.TRUE.equals(holidayMono
                .flatMapMany(Flux::fromIterable)
                .filter(holiday -> holiday.getDate().equals(date))
                .hasElements()
                .block());
    }


    private LocalDateTime nextWeekdayStart(LocalDateTime dateTime) {
        while (isWeekend(dateTime)) {
            dateTime = dateTime.plusDays(1).with(LocalTime.MIDNIGHT);
        }
        return dateTime.with(DAILY_INTERVALS.get(0).start);
    }

    private LocalDateTime nextValidStart(LocalDateTime dateTime) {
        LocalDateTime nextDay = dateTime.plusDays(1).with(LocalTime.MIDNIGHT);
        return nextWeekdayStart(nextDay);
    }

    private LocalDateTime alignToValidTime(LocalDateTime dateTime) {
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
