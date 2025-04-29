package com.scape.creator;

import java.util.List;

import com.scape.room.RoomDTO;
import com.scape.room.RoomService;

public class CreatorService {
    private final CreatorDAO creatorDao = new CreatorDAO();
    private final RoomService roomService = new RoomService();

    public String registerCreator(CreatorDTO creator) {
        boolean isSuccess = creatorDao.insertCreator(creator);
        return isSuccess ? "개발자 등록이 완료되었습니다!" : "개발자 등록에 실패했습니다.";
    }

    public boolean loginCreator(String id, String pw) {
        return creatorDao.checkLogin(id, pw);
    }

    // 방 목록
    public List<RoomDTO> getMyRooms(String creatorId) {
        return roomService.getRoomsByCreator(creatorId);
    }

    // 방 생성
    public boolean createRoom(RoomDTO room) {
        return roomService.createRoom(room);
    }

    // 방 삭제
    public boolean deleteRoom(String roomId, String creatorId) {
        return roomService.deleteRoom(roomId, creatorId);
    }

    // 배정 요청
    public boolean requestAssignment(String roomId, String creatorId, String hopeStore) {
        return roomService.requestAssignment(roomId, creatorId, hopeStore);
    }
}
