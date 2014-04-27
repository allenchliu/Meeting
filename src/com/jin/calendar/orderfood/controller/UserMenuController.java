package com.jin.calendar.orderfood.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jin.calendar.orderfood.bo.MenuEvent;
import com.jin.calendar.orderfood.common.CommonConstant;
import com.jin.calendar.orderfood.model.UserMenu;

/**
 * 订单控制器
 * @author JinLiang
 * @datetime 2014年4月26日 下午8:55:23
 */
public class UserMenuController extends Controller {
	
	private static Logger logger = Logger.getLogger(UserMenuController.class);
	
	/**
	 * 获取时间段内的订单
	 */
	public void getDurationEvent(){
		List<UserMenu> list= UserMenu.dao.getDurationOrder(getParaToInt("userId"), getParaToLong("start"), getParaToLong("end"));
		List<MenuEvent>events=new ArrayList<>();
		try {
			Date today = getToday();
			for (UserMenu userMenu : list) {
				MenuEvent event = new MenuEvent(userMenu.getInt("id"),
						userMenu.getStr("menu_name"),
						userMenu.getTimestamp("order_date"), 
						userMenu.getFloat("price"),
						userMenu.getInt("state"),
						userMenu.getInt("menuid"));
				if(userMenu.getTimestamp("order_date").before(today)){
					event.setColor(CommonConstant.COLOR_FOR_PAST_EVENT);
				} else if(userMenu.getTimestamp("order_date").after(today)){
					event.setColor(CommonConstant.COLOR_FOR_FUTURE_EVENT);
				} else{
					if(new Date().before(getForeRuleDate())){
						event.setColor(CommonConstant.COLOR_FOR_FUTURE_EVENT);
					}else if(new Date().after(getAfterRuleDate())){
						event.setColor(CommonConstant.COLOR_FOR_PAST_EVENT);
					}else{
						if(userMenu.getInt("state")==1){
							event.setColor(CommonConstant.COLOR_FOR_PAST_EVENT);
						}else{
							event.setColor(CommonConstant.COLOR_FOR_FUTURE_EVENT);
						}
					}
				}
				events.add(event);
			}
		} catch (ParseException e) {
			logger.error(e.getMessage());
			renderError(500);
			return;
		}
		renderJson(events);
	}

	/**
	 * 新增订单
	 */
	public void addFoodEvent(){
		Map<String, Object> returnMap=new HashMap<>();
		returnMap.put("isSuccess", false);
		returnMap.put("msgOption", 0);
		Date start = getParaToDate("start");
		try {
			returnMap = isLegalOrder(getParaToInt("state"), start);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			renderError(500);
			return;
		}
		
		if((boolean) returnMap.get("flag")){
			UserMenu u=UserMenu.dao.getSingleOrder(getParaToInt("userId"), getParaToInt("state"), getPara("start"));
			if(u==null){
				boolean isSuccess = new UserMenu().set("userid", getParaToInt("userId"))
						.set("menuid", getParaToInt("menuId"))
						.set("state", getParaToInt("state"))
						.set("order_date", start)
						.set("create_date", new Date())
						.save();
				returnMap.put("isSuccess", isSuccess);
				if(! isSuccess){
					returnMap.put("msg", "提交失败！");
				}else{
					MenuEvent event=getSingleEvent(getParaToInt("userId"), getParaToInt("state"), DateFormatUtils.format(start, "yyyy-MM-dd"));
					event.setColor(CommonConstant.COLOR_FOR_FUTURE_EVENT);
					returnMap.put("event", event);
				}
			}else{
				if(u.getInt("menuid")==getParaToInt("menuId")&&u.getInt("state")==getParaToInt("state")){
					renderNull();
					return;
				}else if(getParaToInt("state")==1){
					returnMap.put("msgOption", 1);
					returnMap.put("msg", getPara("start")+"午餐已定，确认更改？");
				}else if(getParaToInt("state")==2){
					returnMap.put("msgOption", 1);
					returnMap.put("msg", getPara("start")+"晚餐已定，确认更改？");
				}else{
					returnMap.put("msg", "参数传递有误！");
				}
			}
		}

		renderJson(returnMap);
	}
	
	/**
	 * 获取指定用户某天中餐/晚餐订单
	 * @param userId 用户ID
	 * @param state 1:中餐；2:晚餐
	 * @param startDate 日期
	 * @return
	 */
	private MenuEvent getSingleEvent(int userId, int state, String startDate){
		UserMenu userMenu=UserMenu.dao.getSingleOrder(userId, state, startDate);
		MenuEvent event=new MenuEvent(userMenu.getInt("id"),
				userMenu.getStr("menu_name"),
				userMenu.getTimestamp("order_date"), 
				userMenu.getFloat("price"),
				userMenu.getInt("state"),
				userMenu.getInt("menuid"));
		return event;
	}
	
	/**
	 * 判断下单时间是否合法
	 * 规则：
	 * 	1.下单当天之前的日期不接受订单；
	 * 	2.下单当天之后的日期正常下单；
	 * 	3.当天11:30后不接受午餐订单；
	 * 	4.当天19:00后不接受晚餐订单。
	 * @param state
	 * @param start
	 * @return
	 * @throws ParseException
	 */
	private Map<String, Object> isLegalOrder(int state, Date start) throws ParseException{
		Map<String, Object> returnMap=new HashMap<>();
		returnMap.put("flag", false);
		Date date=new Date();
		if(start.before(getToday())){
			returnMap.put("msg", "选择的日期已过期，不接受订单！");
		}else if(start.after(getToday())){
			returnMap.put("flag", true);
		}else if(state==1&&date.after(getForeRuleDate())){
			returnMap.put("msg", "抱歉，今天已不接受午餐订单！");
		}else if(state==2&&date.after(getAfterRuleDate())){
			returnMap.put("msg", "抱歉，今天已不接受晚餐订单！");
		}else if(state==1||state==2){
			returnMap.put("flag", true);
		}else{
			returnMap.put("msg", "参数传递有误");
		}
		return returnMap;
	}
	
	private Date getToday() throws ParseException {
		return DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
	}

	private Date getForeRuleDate() throws ParseException {
		return DateUtils.addMinutes(getToday(), 690);
	}

	private Date getAfterRuleDate() throws ParseException {
		return DateUtils.addHours(getToday(), 19);
	}
}
