package com.jin.calendar.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.jfinal.core.Controller;
import com.jin.calendar.bo.RoomEvent;
import com.jin.calendar.common.CommonConstant;
import com.jin.calendar.model.Room;
import com.jin.calendar.model.RoomSchedule;
import com.jin.calendar.model.User;

public class RoomScheduleController extends Controller {

    public void index() {
        setAttr("roomList", Room.dao.getAllRoom());
        setAttr("ServerTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // render("bookRoom.jsp");
        render("index.jsp");
    }

    public void add() {
        p("test add");
        p(getPara("stime"));
        render("index.jsp");
    }

    public void delete() {
        p("test delete");
        p(getPara("eid"));
        render("index.jsp");
    }

    public void update() {
        p("test update");
        p(getPara("eid"));
        render("index.jsp");
    }

    public void load() {
        p("test load");
        renderJson("[{ start_date: \"2015-04-21 09:00\", end_date: \"2015-04-21 12:00\", text:\"test lesson\", subject: 'math' },]");
    }

    private void p(String str) {
        System.out.println(str);
    }

    public void getDurationEvent() {
        List<RoomSchedule> list = RoomSchedule.dao.getDurationEventsByRoomId(getParaToInt("roomId"), getParaToLong("start"), getParaToLong("end"));
        List<RoomEvent> events = new ArrayList<>();
        for (RoomSchedule roomSchedule : list) {
            RoomEvent event = new RoomEvent(roomSchedule.getInt("id"), roomSchedule.getStr("subject"), roomSchedule.getTimestamp("start"),
                    roomSchedule.getTimestamp("end"), roomSchedule.getStr("username"), roomSchedule.getStr("roomname"), roomSchedule.getStr("email"), false);
            if (roomSchedule.getTimestamp("start").before(new Date())) {
                event.setColor(CommonConstant.COLOR_FOR_PAST_EVENT);
            }
            else if (roomSchedule.getInt("userid").equals(getSessionAttr("userId"))) {
                event.setColor(CommonConstant.COLOR_FOR_OWN_FUTURE_EVENT);
                event.setEditable(true);
            }
            else {
                event.setColor(CommonConstant.COLOR_FOR_FUTURE_EVENT);
            }
            events.add(event);
        }

        renderJson(events);
    }

    public void updateRoomEvent() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSuccess", true);
        Date start = new Date(getParaToLong("start"));
        Date end = new Date(getParaToLong("end"));
        if (start.before(new Date())) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "The start time is already passed. Please choose a new time slot.");
        }
        else if (!RoomSchedule.dao.isLegalEvent(getParaToLong("start") / 1000, getParaToLong("end") / 1000, getParaToInt("roomId"), getParaToInt("id"))) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "Conflict with other meetings.");
        }
        else {
            RoomSchedule roomSchedule = RoomSchedule.dao.findById(getParaToInt("id"));
            roomSchedule.set("start", start);
            roomSchedule.set("end", end);
            roomSchedule.update();
        }
        renderJson(returnMap);
    }

    public void addRoomEvent() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("isSuccess", true);
        Date start = new Date(getParaToLong("start"));
        Date end = new Date(getParaToLong("end"));
        String title = getPara("title");
        String userName = getPara("userName");
        String password = getPara("password");
        int roomId = getParaToInt("roomId");
        if (start.before(new Date())) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "The start time is already passed. Please choose a new time slot.");
        }
        else if (!RoomSchedule.dao.isLegalEvent(getParaToLong("start") / 1000, getParaToLong("end") / 1000, roomId, -100)) {
            returnMap.put("isSuccess", false);
            returnMap.put("msg", "Not a legal event. Please check again.");
        }
        else {
            User user = new User();
            user.set("name", userName).set("password", password).set("create_date", new Date()).save();
            getSession().setAttribute("userId", user.getInt("id"));
            getSession().setAttribute("username", user.getStr("userName"));
            setCookie(new Cookie("userId", "" + user.getInt("id")));
            new RoomSchedule().set("start", start).set("end", end).set("subject", userName).set("userid", user.get("id")).set("roomid", roomId)
                    .set("create_date", new Date()).save();
        }
        renderJson(returnMap);
    }
}
