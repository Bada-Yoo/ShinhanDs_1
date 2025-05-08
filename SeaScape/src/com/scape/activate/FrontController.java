package com.scape.activate;

import java.util.Scanner;

import com.scape.activate.ActivateControllerInterface;

public class FrontController {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);				
        boolean isStop = false;
        ActivateControllerInterface controller = null;

        while (!isStop) {
        	System.out.println("+------------------------------------------+");
        	System.out.println("|       🌊 Seascape에 오신 걸 환영합니다 🌊     |");
        	System.out.println("+------------------------------------------+");
        	System.out.println("   당신의 역할을 선택해주세요 👇");
        	System.out.println("   1. 👤 users       2. 🛠️ creator");
        	System.out.println("   3. 🏪 store       4. 🧑‍💼 boss");
        	System.out.println("   종료하려면 'end'를 입력하세요.");
        	System.out.print("\n👉 입력: ");

            
            String job = sc.next();
            switch (job) {
                case "users"   -> controller = ControllerFactory.make("users");
                case "creator" -> controller = ControllerFactory.make("creator");
                case "store"   -> controller = ControllerFactory.make("store");
                case "boss"    -> controller = ControllerFactory.make("boss");
                case "end"     -> { isStop = true; continue; }
                default        -> { continue; }
            }
            controller.execute();
        }
        sc.close();
        System.out.println("👋 다음에 또 오세요! Seascape와 함께했던 시간, 잊지 마세요 🌅");
    }
}
