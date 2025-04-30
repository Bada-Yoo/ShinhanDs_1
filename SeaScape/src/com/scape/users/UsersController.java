package com.scape.users;

import com.scape.room.RoomDTO;
import com.scape.ReservationSchedule.ReservationScheduleDTO;
import com.scape.ReservationSchedule.ReservationScheduleService;
import com.scape.store.StoreService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.scape.activate.ActivateControllerInterface;

public class UsersController implements ActivateControllerInterface {

    Scanner sc = new Scanner(System.in);
    UsersService service = new UsersService();
    
    private final StoreService storeService = new StoreService();
    private final ReservationScheduleService scheduleService = new ReservationScheduleService();

    private String loginId = null;  // 현재 로그인한 사용자 ID 저장
    
    @Override
    public void execute() {
    	while(true) {
	        System.out.println("1. 회원가입 2. 로그인");
	        String job = sc.next();
	
	        switch (job) {
	            case "1" -> join();
	            case "2" -> login();
	            default -> UsersView.display("잘못된 입력입니다.");
        	}
        }
    }

    private void join() {
        System.out.println("==== 회원가입 ====");
        System.out.print("아이디 입력: ");
        String id = sc.nextLine();
        System.out.print("비밀번호 입력: ");
        String pw = sc.nextLine();
        System.out.print("이름 입력: ");
        String name = sc.nextLine();
        System.out.print("닉네임 입력: ");
        String nickname = sc.nextLine();
        System.out.print("나이 입력: ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.print("방탈출 경험 횟수 입력: ");
        int escapeHistory = sc.nextInt();
        sc.nextLine();

        UsersDTO newUser = UsersDTO.builder()
                .USER_ID(id)
                .USER_PW(pw)
                .USER_NAME(name)
                .USER_NICKNAME(nickname)
                .USER_AGE(age)
                .ESCAPE_HISTORY(escapeHistory)
                .build();

        String result = service.insertId(newUser);
        if (result == null) {
            UsersView.display("회원가입에 실패했습니다.");
        } else if (result.equals("이미 존재하는 아이디입니다.")) {
            UsersView.display(result);
        } else {
            UsersView.display(result + "님, 회원가입을 축하합니다!");
            System.out.println("로그인을 다시 진행해주세요.");
            login();
        }
    }

    private void login() {
        System.out.println("==== 로그인 ====");
        sc.nextLine();
        System.out.print("아이디 입력: ");
        String id = sc.nextLine();
        System.out.print("비밀번호 입력: ");
        String pw = sc.nextLine();

        UsersDTO user = service.login(id, pw);
        if (user != null) {
        	loginId = user.getUSER_ID(); // 로그인 성공 시 ID 저장 // 나중에 사용할거에요
            String message = user.getUSER_NICKNAME() + "님, 로그인 성공했습니다!";
            UsersView.display(message);
            userMenu();
        } else {
            UsersView.display("로그인 실패! 아이디 또는 비밀번호를 확인하세요.");
        }
    }

    private void userMenu() {
        while (true) {
        	
            System.out.println("=== 유저 메뉴 ===");
            System.out.println("1. 예약하기 2. 예약취소 3. 계정삭제 4. 가맹점 문의 5. 로그아웃");
            int menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1 -> makeReservation();
                case 2 -> cancelReservation();
                case 3 -> {
                    boolean isDeleted = deleteAccount();
                    if (isDeleted) {
                        return; // 삭제 성공하면 userMenu 빠져나가서 FrontController로 돌아간다.
                    }
                }
                case 4 -> requestFranchise();
                case 5 -> {
                    logout();
                    return;
                }
                default -> UsersView.display("잘못된 입력입니다.");
            }
        }
    }

    private void makeReservation() {
        if (loginId == null) {
            UsersView.display("로그인이 필요한 기능입니다.");
            return;
        }

        int userAge = service.getUserAge(loginId);

        List<String> stores = storeService.getAllLocations();
        UsersView.display("예약 가능한 매장 목록:");
        for (int i = 0; i < stores.size(); i++) {
            UsersView.display((i + 1) + ". " + stores.get(i));
        }

        int storeIndex = -1;
        while (true) {
            UsersView.display("매장 번호를 입력하세요:");
            try {
                storeIndex = Integer.parseInt(sc.nextLine()) - 1;
                if (storeIndex >= 0 && storeIndex < stores.size()) break;
            } catch (Exception ignored) {}
            UsersView.display("[오류] 잘못된 입력입니다.");
        }
        String store = stores.get(storeIndex);

        List<String> dates = scheduleService.getAvailableDatesByStore(store);
        UsersView.display("예약 가능한 날짜:");
        for (String d : dates) {
            UsersView.display("- " + d);
        }

        String date = null;
        while (true) {
            UsersView.display("날짜를 입력하세요 (예: 2025-05-01):");
            date = sc.nextLine().trim();
            if (date.matches("\\d{4}-\\d{2}-\\d{2}")) break;
            UsersView.display("[오류] 잘못된 입력입니다.");
        }

        Map<RoomDTO, List<ReservationScheduleDTO>> roomMap = scheduleService.getAvailableSchedulesByStoreAndDate(store, date);
        List<RoomDTO> roomList = new ArrayList<>(roomMap.keySet());

        for (RoomDTO room : roomList) {
            UsersView.display("\n[방 이름] " + room.getROOM_NAME());
            UsersView.display("시놉시스: " + room.getSYNOPSIS());
            UsersView.display("19금 여부: " + (room.getIS_19() == 1 ? "예" : "아니오"));
            UsersView.display("장르: " + room.getGENRE());
            UsersView.display("가격: " + room.getPRICE() + "원");

            StringBuilder times = new StringBuilder("예약 가능 시간: ");
            for (ReservationScheduleDTO s : roomMap.get(room)) {
                if (date.equals(LocalDate.now().toString())) {
                    if (LocalTime.parse(s.getRESERVATION_TIME()).isBefore(LocalTime.now())) continue;
                }
                times.append("[").append(s.getRESERVATION_TIME()).append("] ");
            }
            UsersView.display(times.toString());
        }

        UsersView.display("\n예약할 방 이름을 입력하세요:");
        String chosenRoomName = sc.nextLine();
        RoomDTO chosenRoom = roomList.stream()
                .filter(r -> r.getROOM_NAME().equals(chosenRoomName))
                .findFirst().orElse(null);

        if (chosenRoom != null && chosenRoom.getIS_19() == 1 && userAge < 19) {
            UsersView.display("[오류] 19세 미만은 선택할 수 없는 테마입니다.");
            return;
        }

        if (chosenRoom == null) {
            UsersView.display("[오류] 잘못된 입력입니다.");
            return;
        }

        String chosenTime = null;
        while (true) {
            UsersView.display("예약할 시간을 입력하세요 (예: 19:10):");
            chosenTime = sc.nextLine().trim();
            if (chosenTime.matches("\\d{2}:\\d{2}")) break;
            UsersView.display("[오류] 잘못된 입력입니다.");
        }

        int scheduleId = scheduleService.getScheduleId(chosenRoom.getROOM_ID(), date, chosenTime);
        if (scheduleId == -1) {
            UsersView.display("[오류] 잘못된 입력입니다.");
            return;
        }

        UsersView.display("예약 인원 수를 입력하세요:");
        int people;
        try {
            people = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            UsersView.display("[오류] 잘못된 입력입니다.");
            return;
        }

        int totalPrice = chosenRoom.getPRICE() * people;
        UsersView.display("총 금액: " + totalPrice + "원");
        UsersView.display("※ 계좌이체: 1000-3626-9033 (예금주: SCAPE)");

        boolean result = scheduleService.reserve(scheduleId, loginId, people);
        if (result) {
            UsersView.display("[예약 완료]");
            UsersView.display("지점: " + store);
            UsersView.display("테마: " + chosenRoom.getROOM_NAME());
            UsersView.display("날짜: " + date);
            UsersView.display("시간: " + chosenTime);
        } else {
            UsersView.display("[실패] 예약 중 문제가 발생했습니다.");
        }

        UsersView.display("→ 메인 메뉴로 돌아갑니다.\n");
    }

    private void cancelReservation() {
        String message = service.cancelReservation();
        UsersView.display(message);
    }

    private boolean deleteAccount() {
        if (loginId == null) {
            UsersView.display("로그인이 필요한 기능입니다.");
            return false;
        }

        System.out.println("※ 주의: 계정을 삭제하면 복구할 수 없습니다.");
        System.out.println("아래 문구를 정확히 입력하세요: [정말로 삭제합니다]");
        String confirm = sc.nextLine();

        if ("정말로 삭제합니다".equals(confirm)) {
            String message = service.deleteAccount(loginId); 
            UsersView.display(message);
            loginId = null; // 삭제 후 로그인 해제
            return true;
        } else {
            UsersView.display("삭제 문구가 일치하지 않아 계정 삭제가 취소되었습니다.");
        }
        return false;
    }


    private void requestFranchise() {
        String message = service.requestFranchise();
        UsersView.display(message);
    }

    private void logout() {
        UsersView.display("로그아웃합니다.");
    }
}
