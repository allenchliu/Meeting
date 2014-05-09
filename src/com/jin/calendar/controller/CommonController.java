package com.jin.calendar.controller;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.jfinal.core.Controller;
import com.jin.calendar.model.Menu;
import com.jin.calendar.model.Room;
import com.jin.calendar.model.User;

public class CommonController extends Controller {

	public void index() {
		if(getSession().getAttribute("userId")==null||getSession().getAttribute("pageType")==null){
			render("login.jsp");
		}else if(getSession().getAttribute("pageType").equals("bookRoom")){
			setAttr("roomList", Room.dao.getAllRoom());
			setAttr("ServerTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			render("bookRoom.jsp");
		}else if(getSession().getAttribute("pageType").equals("orderFood")){
			setAttr("menuList", Menu.dao.getForenoonFoods());
			setAttr("ServerTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			render("orderFood.jsp");
		}
	}
	
	public void login(){
		User user=User.dao.getUserByLoginName(getPara("username"));
		if(user==null){
			setAttr("msg", "用户名不存在");
			render("login.jsp");
			return;
		}else if(! user.getStr("password").equals(getPara("password"))){
			setAttr("msg", "用户名或密码错误");
			render("login.jsp");
			return;
		}else{
			getSession().setAttribute("userId", user.getInt("id"));
			getSession().setAttribute("pageType", getPara("pageType"));
			redirect("/");
		}
	}
	
	public void exit(){
		getSession().invalidate();
		render("login.jsp");
	}
	
}
