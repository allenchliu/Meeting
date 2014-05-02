package com.jin.calendar.orderfood.controller;

import com.jfinal.core.Controller;
import com.jin.calendar.orderfood.model.RoomSchedule;
import com.jin.calendar.orderfood.model.UserMenu;

public class AdminController extends Controller {

	public void index(){
		setAttr("orderList", UserMenu.dao.getTodayOrder());
		setAttr("meetingList", RoomSchedule.dao.getTodayEvent());
		renderJsp("index.jsp");
	}
	
}
