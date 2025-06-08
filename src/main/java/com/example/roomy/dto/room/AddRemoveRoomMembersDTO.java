package com.example.roomy.dto.room;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AddRemoveRoomMembersDTO {
    @NotNull
    @NotEmpty
    List<Long> userIds;
}
