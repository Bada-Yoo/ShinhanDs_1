package com.scape.creator;

import java.util.List;
import java.util.Scanner;
import com.scape.activate.ActivateControllerInterface;
import com.scape.room.RoomDTO;

public class CreatorController implements ActivateControllerInterface {

    Scanner sc = new Scanner(System.in);
    CreatorService service = new CreatorService();
    private String loginId = null;  // 로그인된 개발자 ID 저장

    @Override
    public void execute() {
        while (true) {
            System.out.println("1. 개발자 등록(회원가입) 2. 개발자 로그인 0. 나가기");
            String job = sc.next();
            sc.nextLine(); // 버퍼 비우기

            switch (job) {
                case "1" -> register();
                case "2" -> login();
                case "0" -> { return; }
                default -> CreatorView.display("잘못된 입력입니다.");
            }
        }
    }

    private void register() {
        System.out.println("==== 개발자 등록 ====");
        System.out.print("ID 입력: ");
        String id = sc.nextLine();
        System.out.print("비밀번호 입력: ");
        String pw = sc.nextLine();
        System.out.print("닉네임 입력: ");
        String nickname = sc.nextLine();

        CreatorDTO creator = CreatorDTO.builder()
                .CREATOR_ID(id)
                .CREATOR_PW(pw)
                .CREATOR_NICKNAME(nickname)
                .build();

        String message = service.registerCreator(creator);
        CreatorView.display(message);
    }

    private void login() {
        System.out.println("==== 개발자 로그인 ====");
        System.out.print("ID 입력: ");
        String id = sc.nextLine();
        System.out.print("비밀번호 입력: ");
        String pw = sc.nextLine();

        CreatorDTO loggedIn = service.loginAndGetCreator(id, pw);
        if (loggedIn != null) {
            loginId = loggedIn.getCREATOR_ID();
            CreatorView.display(loggedIn.getCREATOR_NICKNAME() + "님 안녕하세요! 로그인 성공했습니다!");
            creatorMenu();
        } else {
            CreatorView.display("로그인 실패! ID 또는 PW를 확인하세요.");
        }
    }


    private void creatorMenu() {
        while (true) {
            System.out.println("=== 개발자 메뉴 ===");
            System.out.println("1. 내가 만든 방 리스트 보기 2. 방 생성하기 3. 방 삭제하기 4. 매장에 방 배정 요청하기 5. 로그아웃");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1 -> viewMyRooms();
                case 2 -> createRoom();
                case 3 -> deleteRoom();
                case 4 -> requestRoomAssignment();
                case 5 -> {
                    logout();
                    return;
                }
                default -> CreatorView.display("잘못된 입력입니다.");
            }
        }
    }

    private void viewMyRooms() {
        List<RoomDTO> rooms = service.getMyRooms(loginId);
        CreatorView.displayRooms(rooms);
    }

    private void createRoom() {
        System.out.println("=== 방 생성 ===");
        System.out.print("방 이름 입력: ");
        String roomName = sc.nextLine();
        System.out.print("장르 입력: ");
        String genre = sc.nextLine();
        System.out.print("19금인가요? (1:예 / 0:아니오): ");
        int is19 = sc.nextInt();
        System.out.print("가격 입력: ");
        int price = sc.nextInt();
        System.out.print("제한 시간 입력(분): ");
        int limitTime = sc.nextInt();
        sc.nextLine();
        System.out.print("시놉시스 입력: ");
        String synopsis = sc.nextLine();

        RoomDTO room = RoomDTO.builder()
                .CREATOR_ID(loginId)           
                .ROOM_NAME(roomName)
                .GENRE(genre)
                .IS_19(is19)
                .PRICE(price)
                .LIMIT_TIME(limitTime)
                .SYNOPSIS(synopsis)
                .STORE_UNIQUE_ID(null)
                .HOPE_STORE("0")
                .STORE_STATUS("0")
                .build();

        boolean isSuccess = service.createRoom(room);
        CreatorView.display(isSuccess ? "방이 성공적으로 생성되었습니다." : "방 생성에 실패했습니다.");
    }


    private void deleteRoom() {
        List<RoomDTO> myRooms = service.getMyRooms(loginId);

        if (myRooms.isEmpty()) {
            CreatorView.display("삭제할 수 있는 방이 없습니다.");
            return;
        }

        System.out.println("==== 내가 만든 방 리스트 ====");
        for (RoomDTO room : myRooms) {
            System.out.printf("- 방 이름: %s | 장르: %s | 제한시간: %d분\n",
                    room.getROOM_NAME(), room.getGENRE(), room.getLIMIT_TIME());
        }

        System.out.print("삭제할 방 이름을 입력하세요: ");
        String nameToDelete = sc.nextLine();

        // 방 이름으로 room_id 찾기
        String roomId = myRooms.stream()
                .filter(r -> r.getROOM_NAME().equals(nameToDelete))
                .map(RoomDTO::getROOM_ID)
                .findFirst()
                .orElse(null);

        if (roomId == null) {
            CreatorView.display("해당 이름의 방을 찾을 수 없습니다.");
            return;
        }

        boolean isSuccess = service.deleteRoom(roomId, loginId);
        CreatorView.display(isSuccess ? "삭제 성공!" : "삭제 실패 또는 권한 없음");
    }


    private void requestRoomAssignment() {
        List<RoomDTO> unassignedRooms = service.getUnassignedRooms(loginId);
        CreatorView.displayUnassignedRooms(unassignedRooms);

        if (unassignedRooms.isEmpty()) {
            return;
        }

        System.out.print("배정 요청할 방 이름 입력: ");
        String roomName = sc.nextLine();

        // ✅ CreatorService를 통해 호출
        List<String> locations = service.getAvailableStoreLocations();
        System.out.println("=== 현재 등록된 매장 위치 목록 ===");
        for (String loc : locations) {
            System.out.println("- " + loc);
        }

        System.out.print("희망 매장명 입력: ");
        String hopeStore = sc.nextLine();

        boolean isSuccess = service.requestAssignmentByRoomName(roomName, loginId, hopeStore);
        CreatorView.display(isSuccess ? "배정 요청 완료!" : "요청 실패 (방 없음 또는 권한 없음)");
    }





    private void logout() {
        CreatorView.display("로그아웃합니다.");
        loginId = null;
    }
}
