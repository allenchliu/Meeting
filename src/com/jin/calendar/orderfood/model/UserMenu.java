package com.jin.calendar.orderfood.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class UserMenu extends Model<UserMenu> {
	public static final UserMenu dao=new UserMenu();

	public List<UserMenu> getDurationOrder(int userId, long start, long end){
		String sqlStr="select x.*,y.name menu_name,y.price from user_menu x left join menu y on x.menuid=y.id where x.userid= "
				+ userId + " and x.order_date>=from_unixtime(" + start
				+ ") and x.order_date<from_unixtime(" + end + ")";
		return UserMenu.dao.find(sqlStr);
	}
	
	public UserMenu getSingleOrder(int userId, int state, String startDate){
		String sqlStr="select x.*,y.name menu_name,y.price from user_menu x left join menu y on x.menuid=y.id where x.userid= "
				+ userId + " and x.state=" + state
				+ " and UNIX_TIMESTAMP(x.order_date)=UNIX_TIMESTAMP('"+startDate+"')";
		return UserMenu.dao.findFirst(sqlStr);
	}
}
