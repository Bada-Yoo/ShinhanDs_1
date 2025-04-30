package com.scape.users;

public class UsersView {

    public static void display(String message) {
        System.out.println(message); // 항상 그대로 출력
    }

    public static void displayInfo(String message) {
        System.out.println("[Seascape] ℹ️ " + message);
    }

    public static void displayError(String message) {
        System.out.println("[Seascape] ❌ " + message);
    }

    public static void displaySuccess(String message) {
        System.out.println("[Seascape] ✅ " + message);
    }

    public static void displayHighlight(String message) {
        System.out.println("🌟 " + message);
    }
}
