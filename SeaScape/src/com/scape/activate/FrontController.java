package com.scape.activate;

import java.util.Scanner;


public class FrontController {

	public static void main(String[] args) {
		//사용자가 누구인지 물어봄
		Scanner sc = new Scanner(System.in);				
		boolean isStop = false;
		ActivateControllerInterface controller = null;
		while(!isStop) {
			System.out.println("뭐 여기 오신거 환영합니다.");
			System.out.println("당신은 누구입니까? (1. 유저 2. 제작자 3. 매장 4. 본부)");
			String job = sc.next();
			switch(job) {
			case "users"->{ controller = ControllerFactory.make("users");}
			case "creator"->{ controller = ControllerFactory.make("creator");}
			case "store"->{ controller = ControllerFactory.make("store");}
			case "boss"->{ controller = ControllerFactory.make("boss");}
			case "end"->{isStop=true;continue;}
			default ->{continue;}
			}
			controller.execute();
		}
		sc.close();
		System.out.println("다음에 또 오세요!");
	}
}






