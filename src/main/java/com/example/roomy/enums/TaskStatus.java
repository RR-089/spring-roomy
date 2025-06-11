package com.example.roomy.enums;


import com.example.roomy.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    BACKLOG("Backlog"),
    SPRINT("Sprint"),
    FINISHED("Finished");

    private final String value;

    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }

        throw new BadRequestException("Unknown task status", null);
    }
}
