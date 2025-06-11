package com.example.roomy.dto.room;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddReplaceRoomLeaderDTO {
    @NotNull
    private Long userId;
}
