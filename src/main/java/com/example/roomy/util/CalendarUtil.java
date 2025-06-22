package com.example.roomy.util;

import com.example.roomy.dto.calendar.GoogleCalendarResponse;
import com.example.roomy.dto.calendar.HolidayDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalendarUtil {

    private final WebClient.Builder webClientBuilder;
    @Value("${google.calendar.api_key}")
    private String apiKey;

    public Mono<List<HolidayDTO>> getHolidays(LocalDate minDate, LocalDate maxDate) {
        log.info("Fetching holidays for date range: {} to {}", minDate, maxDate);

        String calendarId = "id.indonesian#holiday@group.v.calendar.google.com"; // Indonesian holidays
        String timeMin = minDate.format(DateTimeFormatter.ISO_DATE) + "T00:00:00Z";
        String timeMax = maxDate.format(DateTimeFormatter.ISO_DATE) + "T23:59:59Z";

        WebClient webClient = webClientBuilder.baseUrl("https://www.googleapis.com/calendar/v3/calendars").build();

        return webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/{calendarId}/events")
                                .queryParam("key", apiKey)
                                .queryParam("timeMin", timeMin)
                                .queryParam("timeMax", timeMax)
                                .queryParam("singleEvents", "true")
                                .build(calendarId))
                        .retrieve()
                        .bodyToMono(GoogleCalendarResponse.class)
                        .flatMap(response -> Mono.just(
                                response.getItems().stream()
                                        .map(event -> HolidayDTO.builder()
                                                                .name(event.getSummary())
                                                                .date(LocalDate.parse(event.getStart().getDate()))
                                                                .build())
                                        .collect(Collectors.toList())
                        ));
    }
}
