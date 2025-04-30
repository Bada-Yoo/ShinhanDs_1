package com.scape.store;

import com.scape.activate.ActivateControllerInterface;
import com.scape.ReservationSchedule.ReservationScheduleDTO;
import com.scape.ReservationSchedule.ReservationScheduleService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class StoreController implements ActivateControllerInterface {

    private final StoreService storeService = new StoreService();
    private final ReservationScheduleService scheduleService = new ReservationScheduleService();
    private final Scanner sc = new Scanner(System.in);
    private String storeId = null;

    @Override
    public void execute() {
        while (true) {
            System.out.println("=== 매장 로그인 ===");
            System.out.println("1. 로그인");
            System.out.println("0. 뒤로 가기");
            String job = sc.nextLine();

            switch (job) {
                case "1" -> login();
                case "0" -> { return; }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void login() {
        System.out.print("매장 ID: ");
        String id = sc.nextLine();
        System.out.print("매장 PW: ");
        String pw = sc.nextLine();

        if (storeService.login(id, pw)) {
            storeId = id;
            System.out.println("로그인 성공!\n");
            menu();
        } else {
            System.out.println("로그인 실패. 다시 시도하세요.");
        }
    }

    private void menu() {
        while (true) {
            System.out.println("=== 매장 메뉴 ===");
            System.out.println("1. 예약 관리");
            System.out.println("2. 예약창 닫기");
            System.out.println("0. 로그아웃");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> viewReservations();
                case "2" -> closeTodayReservations();
                case "0" -> {
                    storeId = null;
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void viewReservations() {
        List<ReservationScheduleDTO> reservations = scheduleService.getReservationsByStoreAndDate(storeId, LocalDate.now(), LocalDate.now().plusDays(1));
        if (reservations == null || reservations.stream().noneMatch(r -> r.getUSER_ID() != null)) {
            System.out.println("당일 및 익일 예약이 없습니다.");
        } else {
            System.out.println("예약 목록:");
            for (ReservationScheduleDTO r : reservations) {
                if (r.getUSER_ID() == null) continue;
                String roomName = scheduleService.getRoomInfo(r.getROOM_ID()).getROOM_NAME();
                String userName = scheduleService.getUserName(r.getUSER_ID());
                System.out.printf("날짜: %s | 시간: %s | 방: %s | 인원: %d | 예약자: %s\n",
                        r.getRESERVATION_DATE(), r.getRESERVATION_TIME(), roomName, r.getHEADCOUNT(), userName);
            }
        }
    }

    private void closeTodayReservations() {
        System.out.print("마감할 날짜를 입력하세요 (예: 2025-05-01): ");
        String input = sc.nextLine().trim();
        try {
            LocalDate target = LocalDate.parse(input);
            LocalDate today = LocalDate.now();
            if (target.isBefore(today) || target.isAfter(today.plusDays(6))) {
                System.out.println("해당 날짜는 마감할 수 없습니다. 오늘부터 7일 이내 날짜만 가능합니다.");
                return;
            }
            boolean result = scheduleService.deleteReservationsByDateAndStore(storeId, target);
            if (result) {
                System.out.println(input + " 예약창이 성공적으로 닫혔습니다.");
            } else {
                System.out.println("실패했습니다. 다시 시도해주세요.");
            }
        } catch (Exception e) {
            System.out.println("날짜 형식이 잘못되었습니다. 예: 2025-05-01");
        }
    }
}
