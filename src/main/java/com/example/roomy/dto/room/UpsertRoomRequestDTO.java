package com.example.roomy.dto.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpsertRoomRequestDTO {
    @NotBlank
    private String name;
}
