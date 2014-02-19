package com.jin.calendar.orderfood.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class RoomSchedule extends Model<RoomSchedule> {
	public static final RoomSchedule dao = new RoomSchedule();

	public List<RoomSchedule> getDurationEventsByRoomId(int roomId, long start, long end) {
		String sqlStr = "select x.*,y.name username from room_schedule x left join user y on x.userid = y.id "
				+ " where x.roomid= "
				+ roomId
				+ " and x.start>=from_unixtime("
				+ start
				+ ") and x.start<from_unixtime(" + end + ")";
		System.out.println(sqlStr);
		return dao.find(sqlStr);
	}
}
