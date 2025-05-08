// BossController.java
package com.scape.boss;

import com.scape.timeslot.TimeSlotService;
import com.scape.activate.ActivateControllerInterface;
import com.scape.room.RoomDTO;
import com.scape.room.RoomService;
import com.scape.store.StoreService;

import java.util.List;
import java.util.Scanner;

public class BossController implements ActivateControllerInterface {
    private final RoomService roomService = new RoomService();
    private final StoreService storeService = new StoreService();
    private final TimeSlotService timeSlotService = new TimeSlotService();
    private final Scanner sc = new Scanner(System.in);
    private static final String ADMIN_PASSWORD = "seascape";

    @Override
    public void execute() {
        System.out.print("🔐 관리자 비밀번호를 입력하세요: ");
        String inputPw = sc.next();

        if (!ADMIN_PASSWORD.equals(inputPw)) {
            System.out.println("❌ 비밀번호가 잘못되었습니다. 메인 화면으로 돌아갑니다.");
            return;
        }

        System.out.println("\n🎉 환영합니다, 관리자 유바다님!");

        while (true) {
            System.out.println("\n+------------------------------------------+");
            System.out.println("|             🧑‍💼 관리자 메뉴 진입             |");
            System.out.println("+------------------------------------------+");
            System.out.println("1. 📮 가맹점 문의 신청서 확인");
            System.out.println("2. 🏘️ 방탈출 입점 신청서 검토");
            System.out.println("3. 🆕 신테마 예약 시간 개장");
            System.out.println("0. 🚪 메인으로 돌아가기");
            System.out.print("\n👉 선택: ");

            String choice = sc.next();

            switch (choice) {
                case "1" -> System.out.println("📮 (미구현) 가맹점 문의 신청서를 확인합니다.");
                case "2" -> processPendingRooms();
                case "3" -> openNewTheme();
                case "0" -> {
                    System.out.println("👋 메인으로 돌아갑니다.");
                    return;
                }
                default -> System.out.println("⚠️ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        }
    }

    public void openNewTheme() {
        List<RoomDTO> noSlotRooms = timeSlotService.getRoomsWithoutTimeSlot();

        if (noSlotRooms.isEmpty()) {
            System.out.println("📭 시간 슬롯이 필요한 방이 없습니다.");
            return;
        }

        System.out.println("\n=== ⏰ 예약 시간 미설정 테마 목록 ===");
        for (RoomDTO room : noSlotRooms) {
            System.out.printf("- 방 이름: %s | 제한 시간: %d분%n", room.getROOM_NAME(), room.getLIMIT_TIME());
        }

        System.out.print("\n📝 예약 시간대를 추가할 방 이름을 입력해주세요: ");
        sc.nextLine();
        String selectedName = sc.nextLine();

        String roomId = noSlotRooms.stream()
            .filter(r -> r.getROOM_NAME().equals(selectedName))
            .map(RoomDTO::getROOM_ID)
            .findFirst()
            .orElse(null);

        if (roomId == null) {
            System.out.println("❌ 해당 이름의 방을 찾을 수 없습니다.");
            return;
        }

        System.out.print("⏰ 예약 시간대를 쉼표(,)로 구분하여 입력해주세요 (예: 10:00,13:00,17:00): ");
        String input = sc.nextLine();
        String[] times = input.split(",");

        for (String time : times) {
            String sqlTime = time.trim();
            boolean success = timeSlotService.insertRoomTimeSlot(roomId, sqlTime);
            if (success) {
                System.out.println("✅ 시간대 " + sqlTime + " 등록 완료");
            } else {
                System.out.println("⚠️ 시간대 " + sqlTime + " 등록 실패 (중복 또는 오류)");
            }
        }
    }

    public void processPendingRooms() {
        List<RoomDTO> pendingRooms = roomService.getPendingRooms();

        for (RoomDTO room : pendingRooms) {
            System.out.println("\n=== 📥 입점 신청 방 정보 ===");
            System.out.println("🏠 방 이름: " + room.getROOM_NAME());
            System.out.println("🎭 장르: " + room.getGENRE());
            System.out.println("📝 시놉시스: " + room.getSYNOPSIS());
            System.out.println("⏳ 제한 시간: " + room.getLIMIT_TIME() + "분");
            System.out.println("💰 가격: " + room.getPRICE() + "원");

            String hopeStore = room.getHOPE_STORE();
            int sizeLimit = storeService.getStoreSizeLimit(hopeStore);
            int currentCount = roomService.getRoomCountInStore(hopeStore);

            System.out.println("📍 희망 매장 위치: " + hopeStore);
            System.out.println("📦 현재 입점 수: " + currentCount + "/" + sizeLimit);

            if (currentCount >= sizeLimit) {
                System.out.println("⛔ 해당 매장은 만실입니다. 자동 반려 처리됩니다.");
                roomService.updateRoomStatus(room.getROOM_ID(), "자리 없음");
                continue;
            }

            System.out.print("✅ 승인하시겠습니까? (1: 승인 / 2: 반려): ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                String uniqueId = storeService.getStoreUniqueIdByLocation(hopeStore);
                roomService.approveRoom(room.getROOM_ID(), uniqueId);
                System.out.println("🎉 입점이 승인되었습니다. store_unique_id: " + uniqueId);
            } else {
                System.out.println("❓ 반려 사유를 선택해주세요:");
                System.out.println("1. 💸 가격이 비쌈\n2. 🛠️ 테마 보완 요청\n3. ✏️ 기타 (직접 입력)");
                int reason = sc.nextInt();
                sc.nextLine();

                String status = switch (reason) {
                    case 1 -> "가격이 비싸";
                    case 2 -> "테마보완요망";
                    case 3 -> {
                        System.out.print("기타 사유 입력: ");
                        yield "기타 - " + sc.nextLine();
                    }
                    default -> "기타 - 사유 불명";
                };

                roomService.updateRoomStatus(room.getROOM_ID(), status);
                System.out.println("❌ 반려 처리 완료. 사유: " + status);
            }
        }
    }
}