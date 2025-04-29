package com.scape.creator;

import java.util.List;
import java.util.Scanner;

import com.scape.room.RoomDAO;
import com.scape.room.RoomDTO;
import com.scape.room.RoomService;

public class CreatorService {
    private final CreatorDAO creatorDao = new CreatorDAO();
    private final RoomService roomService = new RoomService();
    private final Scanner sc = new Scanner(System.in);

    public String registerCreator(CreatorDTO creator) {
        boolean isSuccess = creatorDao.insertCreator(creator);
        return isSuccess ? "개발자 등록이 완료되었습니다!" : "개발자 등록에 실패했습니다.";
    }

    public boolean loginCreator(String id, String pw) {
        return creatorDao.checkLogin(id, pw);
    }

    public void viewRooms(String creatorId) {
        roomService.viewRooms(creatorId);
    }

    public void createRoom(String creatorId) {
        roomService.createRoom(creatorId);
    }

    public void deleteRoom(String creatorId) {
        roomService.deleteRoom(creatorId);
    }

    public void requestAssignment(String creatorId) {
        roomService.requestAssignment(creatorId);
    }
}