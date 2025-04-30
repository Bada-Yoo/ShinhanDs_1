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

    public CreatorDTO loginAndGetCreator(String id, String pw) {
        boolean result = creatorDao.checkLogin(id, pw);
        if (!result) return null;

        return creatorDao.findCreatorById(id);
    }


    public List<RoomDTO> getMyRooms(String creatorId) {
        return roomService.getRoomsByCreator(creatorId);
    }

    public boolean createRoom(RoomDTO room) {
        return roomService.createRoom(room);
    }

    public boolean deleteRoom(String roomId, String creatorId) {
        return roomService.deleteRoom(roomId, creatorId);
    }

    public List<RoomDTO> getUnassignedRooms(String creatorId) {
        return roomService.getUnassignedRooms(creatorId);
    }

    public boolean requestAssignmentByRoomName(String roomName, String creatorId, String hopeStore) {
        List<RoomDTO> myRooms = roomService.getUnassignedRooms(creatorId);
        
        for (RoomDTO room : myRooms) {
            if (room.getROOM_NAME().equals(roomName)) {
                return roomService.requestAssignment(room.getROOM_ID(), creatorId, hopeStore);
            }
        }
        return false; 
    }

    public List<String> getAvailableStoreLocations() {
        return roomService.getAvailableStoreLocations(); 
    }
}
