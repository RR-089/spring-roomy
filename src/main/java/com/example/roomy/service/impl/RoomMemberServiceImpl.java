package com.example.roomy.service.impl;

import com.example.roomy.exception.NotFoundException;
import com.example.roomy.model.Room;
import com.example.roomy.model.User;
import com.example.roomy.repository.RoomRepository;
import com.example.roomy.service.RoomMemberService;
import com.example.roomy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomMemberServiceImpl implements RoomMemberService {
    private final RoomRepository roomRepository;
    private final UserService userService;

    @Override
    public void bulkAddRoomMembers(Long roomId, List<Long> userIds) {
        log.info("req bulk add room members, roomId: {}, userIds: {}", roomId, userIds);

        Room foundRoom = roomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException("Room not found", null)
        );

        List<User> users = userService.findUsersByIds(userIds);

        for (User user : users) {
            foundRoom.addRoomMember(user);
        }

        roomRepository.save(foundRoom);
    }

    @Override
    public void bulkRemoveRoomMembers(Long roomId, List<Long> userIds) {
        log.info("req bulk remove room members, roomId: {}, userIds: {}", roomId,
                userIds);

        Room foundRoom = roomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException("Room not found", null)
        );

        List<User> users = userService.findUsersByIds(userIds);

        for (User user : users) {
            foundRoom.removeRoomMember(user);
        }

        roomRepository.save(foundRoom);
    }
}
