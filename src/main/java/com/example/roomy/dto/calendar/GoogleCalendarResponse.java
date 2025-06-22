package com.example.roomy.dto.calendar;

import lombok.Data;

import java.util.List;

@Data
public class GoogleCalendarResponse {
    private List<GoogleCalendarEvent> items;

    @Data
    public static class GoogleCalendarEvent {
        private String summary;
        private GoogleCalendarEventDate start;

        @Data
        public static class GoogleCalendarEventDate {
            private String date;
        }
    }
}
