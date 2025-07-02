package com.example.roomy.service.impl;

import com.example.roomy.constant.RoleConstant;
import com.example.roomy.dto.common.OptionDTO;
import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.room.*;
import com.example.roomy.enums.RoomStatus;
import com.example.roomy.exception.BadRequestException;
import com.example.roomy.exception.NotFoundException;
import com.example.roomy.model.Room;
import com.example.roomy.model.User;
import com.example.roomy.repository.RoomRepository;
import com.example.roomy.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public static RoomDTO mapToDTO(Room room) {
        return RoomDTO.builder()
                      .id(room.getId())
                      .name(room.getName())
                      .status(room.getStatus().toString())
                      .roomMaster(
                              room.getRoomMaster() != null ?
                                      UserServiceImpl.mapToDTO(room.getRoomMaster())
                                      : null
                      ).roomMembers(
                        room.getRoomMembers() != null ?
                                room.getRoomMembers()
                                    .stream()
                                    .map(UserServiceImpl::mapToInfoDTO)
                                    .collect(Collectors.toSet()) : new HashSet<>())
                      .build();
    }

    public static RoomInfoDTO mapToInfoDTO(Room room) {
        return RoomInfoDTO.builder()
                          .id(room.getId())
                          .name(room.getName())
                          .status(room.getStatus().toString())
                          .roomMaster(
                                  room.getRoomMaster() != null ?
                                          UserServiceImpl.mapToDTO(room.getRoomMaster())
                                          : null
                          )
                          .build();
    }


    @Override
    public PaginationDTO<List<RoomDTO>> findAllRooms(GetRoomsRequestDTO dto,
                                                     Pageable pageable) {
        log.info("req all rooms data");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        Set<String> roles = auth.getAuthorities()
                                .stream().map(Object::toString).collect(Collectors.toSet());

        boolean isFullRead = roles.stream()
                                  .anyMatch(role -> RoleConstant.NO_READ_RESTRICTIONS
                                          .stream()
                                          .anyMatch(role::contains));

        Page<Room> roomPage = roomRepository.findAllRooms(
                dto.getSearch(),
                dto.getIds(),
                dto.getNames(),
                dto.getStatuses(),
                dto.getRoomMasterIds(),
                dto.getRoomMemberIds(),
                isFullRead ? null : username,
                pageable
        );

        List<RoomDTO> roomDTOS = roomPage.getContent().stream()
                                         .map(RoomServiceImpl::mapToDTO)
                                         .toList();

        return PaginationDTO.<List<RoomDTO>>builder()
                            .totalRecords(roomPage.getTotalElements())
                            .totalPages(roomPage.getTotalPages())
                            .data(roomDTOS)
                            .build();

    }

    @Override
    public GetRoomsOptionsResponseDTO findRoomsOptions(GetRoomsOptionsRequestDTO dto) {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        List<OptionDTO<Long>> ids = new ArrayList<>();
        List<OptionDTO<String>> names = new ArrayList<>();
        List<OptionDTO<String>> statuses = new ArrayList<>();
        List<OptionDTO<Long>> roomMasterIds = new ArrayList<>();
        List<OptionDTO<Long>> roomMemberIds = new ArrayList<>();

        for (Room room : rooms) {
            if (dto.isIds()) {
                ids.add(OptionDTO.buildOption(room.getName(), room.getId()));
            }

            if (dto.isNames()) {
                names.add(OptionDTO.buildOption(room.getName(), room.getName()));
            }

            if (dto.isStatuses()) {
                statuses.add(OptionDTO.buildOption(room.getStatus().toString(),
                        room.getStatus().toString()));
            }

            if (dto.isRoomMasterIds() && room.getRoomMaster() != null) {
                roomMasterIds.add(OptionDTO.buildOption(room.getRoomMaster().getUsername(), room.getRoomMaster().getId()));
            }

            if (dto.isRoomMemberIds()) {
                for (User user : room.getRoomMembers()) {
                    roomMemberIds.add(OptionDTO.buildOption(user.getUsername(),
                            user.getId()));
                }
            }
        }

        return GetRoomsOptionsResponseDTO.builder()
                                         .ids(ids)
                                         .names(names)
                                         .statuses(statuses.stream().distinct().toList())
                                         .roomMasterIds(roomMasterIds)
                                         .roomMemberIds(roomMemberIds)
                                         .build();
    }

    @Override
    public Room findEntityById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException("Room not found", null)
        );
    }

    @Override
    public RoomDTO upsertRoom(Long roomId, UpsertRoomRequestDTO dto) {
        Room room = roomRepository.findById(roomId).orElseGet(() -> null);

        if (room == null) {
            room = Room.builder()
                       .name(dto.getName())
                       .status(RoomStatus.BACKLOG)
                       .build();
        } else {
            BeanUtils.copyProperties(dto, room);
        }

        return mapToDTO(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new NotFoundException("Room not found", null)
        );

        if (room.getStatus() != RoomStatus.BACKLOG) {
            throw new BadRequestException("Can't delete room except room with status " +
                    "Backlog", null);
        }

        roomRepository.delete(room);
    }
}
