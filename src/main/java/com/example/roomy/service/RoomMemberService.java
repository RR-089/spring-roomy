package com.example.roomy.service;

import java.util.List;

public interface RoomMemberService {
    void bulkAddRoomMembers(Long roomId, List<Long> userIds);

    void bulkRemoveRoomMembers(Long roomId, List<Long> userIds);
}
