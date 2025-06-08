package com.example.roomy.controller;

import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.room.GetRoomsRequestDTO;
import com.example.roomy.dto.room.RoomDTO;
import com.example.roomy.dto.room.UpsertRoomRequestDTO;
import com.example.roomy.service.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ResponseDTO<PaginationDTO<List<RoomDTO>>>> getAllRooms(GetRoomsRequestDTO dto, Pageable pageable) {
        PaginationDTO<List<RoomDTO>> data = roomService.findAllRooms(dto, pageable);

        return ResponseEntity.ok(
                ResponseDTO.<PaginationDTO<List<RoomDTO>>>builder()
                           .status(HttpStatus.OK.value())
                           .message("Get rooms successful")
                           .data(data)
                           .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOM_MASTER')")
    public ResponseEntity<ResponseDTO<RoomDTO>> createRoom(
            @RequestBody UpsertRoomRequestDTO dto
    ) {
        RoomDTO data = roomService.upsertRoom(0L, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseDTO.<RoomDTO>builder()
                           .status(HttpStatus.CREATED.value())
                           .message("Create room successful")
                           .data(data)
                           .build()
        );
    }

    @PatchMapping(value = "/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOM_MASTER')")
    public ResponseEntity<ResponseDTO<RoomDTO>> updateRoom(
            @PathVariable("roomId") Long roomId,
            @RequestBody UpsertRoomRequestDTO dto
    ) {
        RoomDTO data = roomService.upsertRoom(roomId, dto);

        return ResponseEntity.ok(
                ResponseDTO.<RoomDTO>builder()
                           .status(HttpStatus.OK.value())
                           .message("Update room successful")
                           .data(data)
                           .build()
        );
    }

    @DeleteMapping(value = "/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO<Object>> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.deleteRoom(roomId);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                           .status(HttpStatus.OK.value())
                           .message("Delete room successful")
                           .data(null)
                           .build()
        );
    }


}
