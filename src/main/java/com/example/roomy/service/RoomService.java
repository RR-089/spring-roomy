package com.example.roomy.service;

import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.room.*;
import com.example.roomy.model.Room;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    PaginationDTO<List<RoomDTO>> findAllRooms(GetRoomsRequestDTO dto, Pageable pageable);

    GetRoomsOptionsResponseDTO findRoomsOptions(GetRoomsOptionsRequestDTO dto);

    Room findEntityById(Long roomId);

    RoomDTO upsertRoom(Long roomId, UpsertRoomRequestDTO dto);

    void deleteRoom(Long roomId);
}
