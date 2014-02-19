package com.jin.calendar.orderfood.controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;
import com.jin.calendar.orderfood.model.Menu;

/**
 * 菜单Controller控制器类
 * @author Jin
 * @datetime 2014年1月16日 上午12:07:00
 */
public class MenuController extends Controller {

	public void getMenu() {
		List<Menu> list = new ArrayList<>();
		if (getParaToInt() == 1) {
			list = Menu.dao.getForenoonFoods();
		} else if (getParaToInt() == 2) {
			list = Menu.dao.getAfternoonFoods();
		} else {
			renderNull();
			return;
		}
		renderJson(list);
	}
}
