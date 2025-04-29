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
                String is19Text = room.getIS_19() == 1 ? "예" : "아니오";

                System.out.printf("방탈출 이름: %s | 장르: %s | 19금: %s | 가격: %d원 | 제한시간: %d분%n",
                        room.getROOM_NAME(),
                        room.getGENRE(),
                        is19Text,
                        room.getPRICE(),
                        room.getLIMIT_TIME());

                System.out.println("시놉시스: " + room.getSYNOPSIS());
                System.out.println("--------------------------------------------------");
            }
        }
    }

}
