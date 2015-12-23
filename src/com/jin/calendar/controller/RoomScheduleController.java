package com.jin.calendar.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.jfinal.core.Controller;
import com.jin.calendar.bo.RoomEvent;
import com.jin.calendar.model.Room;
import com.jin.calendar.model.RoomSchedule;

public class RoomScheduleController extends Controller {

    public void index() {
        setAttr("roomList", Room.dao.getAllRoom());
        setAttr("ServerTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // render("bookRoom.jsp");
        render("index.jsp");
    }

    public void delete() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSuccess", true);
        p("test delete");
        RoomSchedule roomSchedule = RoomSchedule.dao.findById(getParaToInt("id"));
        roomSchedule.delete();
        renderJson(returnMap);
    }

    private void p(String str) {
        System.out.println(str);
    }

    public void load() {
        p("test load");
        List<RoomSchedule> list = RoomSchedule.dao.getDurationEventsByRoomId(getParaToInt("roomId"), getPara("start"), getPara("end"));
        List<RoomEvent> events = new ArrayList<>();
        for (RoomSchedule roomSchedule : list) {
            RoomEvent event = new RoomEvent(roomSchedule.getInt("id"), roomSchedule.getStr("subject"), roomSchedule.getTimestamp("start_date"),
                    roomSchedule.getTimestamp("end_date"), roomSchedule.getStr("username"), roomSchedule.getStr("roomname"), roomSchedule.getStr("email"),
                    false);
            // event.setColor("green");
            events.add(event);
        }
        renderJson(events);
    }

    public void update() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSuccess", true);
        Date start = new Date(getPara("start_date"));
        Date end = new Date(getPara("end_date"));
        if (start.before(new Date())) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "The start time is already passed. Please choose a new time slot.");
        }
        else if (!RoomSchedule.dao.isLegalEvent(start.getTime() / 1000, end.getTime() / 1000, getParaToInt("roomId"), getParaToLong("id"))) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "Conflict with other meetings.");
        }
        else {
            RoomSchedule roomSchedule = RoomSchedule.dao.findById(getParaToLong("id"));
            if (roomSchedule == null) {
                returnMap.put("isSuccess", false);
                returnMap.put("msg", "The event doesn't exist, please refresh your page.");
            }
            else {
                roomSchedule.set("start_date", start);
                roomSchedule.set("end_date", end);
                roomSchedule.set("username", getPara("userName"));
                roomSchedule.set("subject", getPara("text"));
                roomSchedule.update();
            }
        }
        renderJson(returnMap);
    }

    public void add() throws ParseException {
        p("test add");
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSuccess", true);
        Date start = new Date(getPara("start_date"));
        Date end = new Date(getPara("end_date"));
        String text = getPara("text");
        String userName = getPara("userName");
        String password = getPara("password");
        int roomId = getParaToInt("roomId");
        if (start.before(new Date())) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "The start time is already passed. Please choose a new time slot.");
        }
        else if (!RoomSchedule.dao.isLegalEvent(start.getTime() / 1000, end.getTime() / 1000, roomId, -100)) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "Not a legal event. Please check again.");
        }
        else {
            // User user = new User();
            // user.set("name", userName).set("password", password).set("create_date", new Date()).save();
            // getSession().setAttribute("userId", user.getInt("id"));
            // getSession().setAttribute("username", user.getStr("userName"));
            // setCookie(new Cookie("userId", "" + user.getInt("id")));
            new RoomSchedule().set("start_date", start).set("end_date", end).set("username", userName).set("password", password).set("subject", text)
                    .set("roomid", roomId).set("create_date", new Date()).save();
        }
        renderJson(returnMap);
    }

}
