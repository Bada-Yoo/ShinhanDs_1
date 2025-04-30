package com.scape.users;

import java.util.Scanner;
import com.scape.activate.ActivateControllerInterface;

public class UsersController implements ActivateControllerInterface {

    Scanner sc = new Scanner(System.in);
    UsersService service = new UsersService();
    
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
        String message = service.makeReservation();
        UsersView.display(message);
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
