package com.example.roomy.enums;

import com.example.roomy.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomStatus {
    BACKLOG("Backlog"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    private final String value;

    public static RoomStatus fromValue(String value) {
        for (RoomStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }

        throw new BadRequestException("Unknown room status", null);
    }
}
