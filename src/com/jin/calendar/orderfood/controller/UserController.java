package com.jin.calendar.orderfood.controller;

import java.util.Date;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jin.calendar.orderfood.bo.DwzResponseBO;
import com.jin.calendar.orderfood.common.CommonConstant;
import com.jin.calendar.orderfood.model.User;

public class UserController extends Controller {
	
	public void index(){
		Page<User> page = User.dao.paginate(CommonConstant.PAGENUMBER, CommonConstant.PAGESIZE, "select *", "from user");
		setAttr("page", page);
		render("/admin/user.jsp");
	}

	public void getPageList(){
		Page<User> page = User.dao.paginate(getParaToInt("pageNum"), getParaToInt("numPerPage"), "select *", "from user");
		setAttr("page", page);
		render("/admin/user.jsp");
	}
	
	public void add(){
		DwzResponseBO responseBO = new DwzResponseBO(getPara("navTabId"),"closeCurrent");
		boolean flag=new User().set("name", getPara("name"))
				.set("loginName", getPara("loginName"))
				.set("password", getPara("password"))
				.set("tel", getPara("tel"))
				.set("email", getPara("email"))
				.set("create_date", new Date())
				.save();
		if(!flag){
			responseBO.setStatusCode("300");
			responseBO.setMessage("操作失败！");
		}
		renderJson(responseBO);
						
	}
	
	public void del(){
		DwzResponseBO responseBO = new DwzResponseBO();
		boolean flag=Db.deleteById("user", getParaToInt());
		if(!flag){
			responseBO.setStatusCode("300");
			responseBO.setMessage("操作失败！");
		}
		renderJson(responseBO);
	}
}
