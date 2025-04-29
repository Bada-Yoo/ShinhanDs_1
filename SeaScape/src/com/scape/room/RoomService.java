package com.scape.room;

import java.util.List;

public class RoomService {
    private final RoomDAO dao = new RoomDAO();

    // 방 목록 조회 (개발자 ID 기준)
    public List<RoomDTO> getRoomsByCreator(String creatorId) {
        return dao.findRoomsByCreator(creatorId);
    }

    // 방 생성
    public boolean createRoom(RoomDTO room) {
        return dao.insertRoom(room);
    }

    // 방 삭제
    public boolean deleteRoom(String roomId, String creatorId) {
        return dao.deleteRoomByCreator(roomId, creatorId);
    }

    // 방 배정 요청
    public boolean requestAssignment(String roomId, String creatorId, String hopeStore) {
        return dao.updateRoomRequest(roomId, creatorId, hopeStore);
    }
}
