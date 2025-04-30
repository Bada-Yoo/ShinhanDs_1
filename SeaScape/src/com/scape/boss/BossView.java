package com.scape.boss;

public class BossView {
    public static void printWelcome() {
        System.out.println("환영합니다 관리자 유바다님!");
    }

    public static void printLoginFailed() {
        System.out.println("[관리자 알림] 비밀번호가 잘못되었습니다. 메인 화면으로 돌아갑니다.");
    }

    public static void printMenu() {
        System.out.println("=== 관리자 메뉴 ===");
        System.out.println("1. 가맹점 문의 신청서");
        System.out.println("2. 방탈출 입점 신청서");
        System.out.println("0. 뒤로 가기");
        System.out.print("선택: ");
    }

    public static void printInvalidChoice() {
        System.out.println("[관리자 알림] 잘못된 입력입니다.");
    }

    public static void printBackToMain() {
        System.out.println("[관리자 알림] 메인으로 돌아갑니다.");
    }

    public static void printFranchiseStub() {
        System.out.println("[관리자 알림] 가맹점 문의 신청서를 확인합니다. (미구현)");
    }
}