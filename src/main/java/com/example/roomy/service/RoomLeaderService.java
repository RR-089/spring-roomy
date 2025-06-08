package com.example.roomy.service;

import com.example.roomy.dto.room.RoomDTO;

public interface RoomLeaderService {

    RoomDTO addRoomLeader(Long roomId, Long userId);

    RoomDTO replaceRoomLeader(Long roomId, Long userId);

}
