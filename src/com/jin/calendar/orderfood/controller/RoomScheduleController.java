package com.jin.calendar.orderfood.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jin.calendar.orderfood.bo.RoomEvent;
import com.jin.calendar.orderfood.common.CommonConstant;
import com.jin.calendar.orderfood.model.RoomSchedule;

public class RoomScheduleController extends Controller {

	public void getDurationEvent() {
		List<RoomSchedule> list = RoomSchedule.dao.getDurationEventsByRoomId(
				getParaToInt("roomId"), getParaToLong("start"),
				getParaToLong("end"));
		List<RoomEvent> events = new ArrayList<>();
		for (RoomSchedule roomSchedule : list) {
			RoomEvent event = new RoomEvent(roomSchedule.getInt("id"),
					roomSchedule.getStr("subject"),
					roomSchedule.getTimestamp("start"),
					roomSchedule.getTimestamp("end"),
					roomSchedule.getStr("username"),
					false);
			if (roomSchedule.getTimestamp("start").before(new Date())) {
				event.setColor(CommonConstant.COLOR_FOR_PAST_EVENT);
			} else if(roomSchedule.getInt("userid").equals(getSessionAttr("userId"))){
				event.setColor(CommonConstant.COLOR_FOR_OWN_FUTURE_EVENT);
				event.setEditable(true);
			} else {
				event.setColor(CommonConstant.COLOR_FOR_FUTURE_EVENT);
			}
			events.add(event);
		}

		renderJson(events);
	}

	public void updateRoomEvent(){
		Map<String, Object> returnMap=new HashMap<>();
		returnMap.put("isSuccess", true);
		Date date=new Date(getParaToLong("start"));
		Date end=new Date(getParaToLong("end"));
		if(date.before(new Date())){
			returnMap.put("isSuccess", false);
			returnMap.put("msg", "调整后的开始时间已过期，无法更新");
		}else{
			RoomSchedule roomSchedule=RoomSchedule.dao.findById(getParaToInt("id"));
			roomSchedule.set("start", date);
			roomSchedule.set("end", end);
			roomSchedule.update();
		}
	}
}
