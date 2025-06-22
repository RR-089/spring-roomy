package com.example.roomy.dto.calendar;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HolidayDTO {
    private String name;
    private LocalDate date;
}
