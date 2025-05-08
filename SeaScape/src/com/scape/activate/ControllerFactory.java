package com.scape.activate;

import com.scape.activate.ActivateControllerInterface;
import com.scape.boss.BossController;
import com.scape.creator.CreatorController;
import com.scape.store.StoreController;
import com.scape.users.UsersController;

public class ControllerFactory {

	public static ActivateControllerInterface make(String business) {
		ActivateControllerInterface controller = null;
		
		switch (business) {
		  case "users"->{controller = new UsersController();}  
		  case "creator"->{controller = new CreatorController();}
		  case "store"->{controller = new StoreController();}
		  case "boss"->{controller = new BossController();}
		}
		
		
		return controller;
	}


}
