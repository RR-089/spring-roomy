package com.example.roomy.controller;

import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.room.*;
import com.example.roomy.service.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Rooms")
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ResponseDTO<PaginationDTO<List<RoomDTO>>>> getAllRooms(
            @RequestParam(value = "type", required = false) String type,
            @ParameterObject GetRoomsRequestDTO dto,
            @ParameterObject Pageable pageable) {
        PaginationDTO<List<RoomDTO>> data = type != null && type.equals("spec") ?
                roomService.findAllRoomsSpecVersion(dto, pageable) : roomService.findAllRooms(dto, pageable);

        return ResponseEntity.ok(
                ResponseDTO.<PaginationDTO<List<RoomDTO>>>builder()
                           .status(HttpStatus.OK.value())
                           .message("Get rooms successful")
                           .data(data)
                           .build()
        );
    }

    @GetMapping(value = "/options")
    public ResponseEntity<ResponseDTO<GetRoomsOptionsResponseDTO>> getRoomsOptions(
            @ParameterObject GetRoomsOptionsRequestDTO dto
    ) {
        GetRoomsOptionsResponseDTO data = roomService.findRoomsOptions(dto);

        return ResponseEntity.ok(
                ResponseDTO.<GetRoomsOptionsResponseDTO>builder()
                           .status(HttpStatus.OK.value())
                           .message("Get rooms options successful")
                           .data(data)
                           .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ROOM_MASTER')")
    public ResponseEntity<ResponseDTO<RoomDTO>> createRoom(
            @Valid @RequestBody UpsertRoomRequestDTO dto
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('ROOM_MASTER')")
    public ResponseEntity<ResponseDTO<RoomDTO>> updateRoom(
            @PathVariable("roomId") Long roomId,
            @Valid @RequestBody UpsertRoomRequestDTO dto
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
    @PreAuthorize("hasRole('ADMIN')")
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
