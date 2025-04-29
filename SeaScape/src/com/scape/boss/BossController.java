package com.scape.boss;
import com.scape.activate.ActivateControllerInterface;

public class BossController implements ActivateControllerInterface{

	 @Override
	 public void execute() {
		 System.out.println("imtheboss");
	 }
}
