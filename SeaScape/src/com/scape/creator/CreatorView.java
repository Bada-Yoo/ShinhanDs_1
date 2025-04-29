package com.scape.creator;

import java.util.List;
import com.scape.room.RoomDTO;

public class CreatorView {

    public static void display(String message) {
        System.out.println("[개발자 알림] " + message);
    }

    public static void displayRooms(List<RoomDTO> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("등록된 방이 없습니다.");
        } else {
            System.out.println("==== 내가 만든 방 리스트 ====");
            for (RoomDTO room : rooms) {
                System.out.println(room);
            }
        }
    }
}
