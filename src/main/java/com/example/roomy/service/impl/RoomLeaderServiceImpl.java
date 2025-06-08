package com.example.roomy.service.impl;

import com.example.roomy.dto.room.RoomDTO;
import com.example.roomy.model.Room;
import com.example.roomy.model.User;
import com.example.roomy.repository.RoomRepository;
import com.example.roomy.service.RoomLeaderService;
import com.example.roomy.service.RoomService;
import com.example.roomy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomLeaderServiceImpl implements RoomLeaderService {
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final UserService userService;


    @Override
    public RoomDTO addRoomLeader(Long roomId, Long userId) {
        log.info("req add room leader, with roomId: {}, and userId: {}", roomId, userId);

        Room foundRoom = roomService.findEntityById(roomId);

        User foundUser = userService.findEntityById(userId);

        foundRoom.setRoomMaster(foundUser);

        return RoomServiceImpl.mapToDTO(roomRepository.save(foundRoom));
    }

    @Override
    public RoomDTO replaceRoomLeader(Long roomId, Long userId) {
        log.info("req replace room leader, with roomId: {}, and userId: {}", roomId,
                userId);

        Room foundRoom = roomService.findEntityById(roomId);

        User foundUser = null;

        if (userId != null) {
            foundUser = userService.findEntityById(userId);
        }

        foundRoom.setRoomMaster(foundUser);

        return RoomServiceImpl.mapToDTO(roomRepository.save(foundRoom));
    }
}
