package com.example.roomy.controller;


import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.room.AddRemoveRoomMembersDTO;
import com.example.roomy.service.RoomMemberService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms/{roomId}/members")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoomMemberController {
    private final RoomMemberService roomMemberService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ROOM_MASTER')")
    public ResponseEntity<ResponseDTO<Object>> bulkAddRoomMembers(
            @PathVariable("roomId") Long roomId,
            @Valid @RequestBody AddRemoveRoomMembersDTO dto
    ) {
        roomMemberService.bulkAddRoomMembers(roomId, dto.getUserIds());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ResponseDTO.builder()
                                              .status(HttpStatus.CREATED.value())
                                              .message("Bulk add members successful")
                                              .data(null)
                                              .build()
                             );
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ROOM_MASTER')")
    public ResponseEntity<ResponseDTO<Object>> bulkRemoveRoomMembers(
            @PathVariable("roomId") Long roomId,
            @Valid @RequestBody AddRemoveRoomMembersDTO dto
    ) {
        roomMemberService.bulkRemoveRoomMembers(roomId, dto.getUserIds());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ResponseDTO.builder()
                                              .status(HttpStatus.CREATED.value())
                                              .message("Bulk remove members successful")
                                              .data(null)
                                              .build()
                             );
    }
}
