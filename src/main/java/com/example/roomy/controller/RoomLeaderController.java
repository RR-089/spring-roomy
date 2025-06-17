package com.example.roomy.controller;

import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.room.AddReplaceRoomLeaderDTO;
import com.example.roomy.dto.room.RoomDTO;
import com.example.roomy.service.RoomLeaderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms/{roomId}/leaders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Room Leaders")
@PreAuthorize("hasRole('ADMIN') or hasRole('ROOM_MASTER')")
public class RoomLeaderController {
    private final RoomLeaderService roomLeaderService;


    @PostMapping
    public ResponseEntity<ResponseDTO<RoomDTO>> addRoomLeader(
            @PathVariable("roomId") Long roomId,
            @Valid @RequestBody AddReplaceRoomLeaderDTO dto
    ) {
        RoomDTO data = roomLeaderService.addRoomLeader(roomId, dto.getUserId());

        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status).body(
                ResponseDTO.<RoomDTO>builder()
                           .status(status.value())
                           .message("Add room leader successful")
                           .data(data)
                           .build()
        );
    }


    @PatchMapping
    public ResponseEntity<ResponseDTO<RoomDTO>> replaceRoomLeader(
            @PathVariable("roomId") Long roomId,
            @Valid @RequestBody AddReplaceRoomLeaderDTO dto
    ) {
        RoomDTO data = roomLeaderService.replaceRoomLeader(roomId, dto.getUserId());

        return ResponseEntity.ok(
                ResponseDTO.<RoomDTO>builder()
                           .status(HttpStatus.OK.value())
                           .message("Replace room leader successful")
                           .data(data)
                           .build()
        );
    }
}
