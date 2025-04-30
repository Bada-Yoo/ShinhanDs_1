package com.scape.users;

public class UsersView {
    private static boolean firstMessage = true;

    public static void display(String message) {
        if (firstMessage) {
            System.out.println("[유저 알림] " + message);
            firstMessage = false;
        } else {
            System.out.println(message);
        }
    }

    public static void resetMessagePrefix() {
        firstMessage = true;
    }
}
