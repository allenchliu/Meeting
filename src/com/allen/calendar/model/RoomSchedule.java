package com.allen.calendar.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class RoomSchedule extends Model<RoomSchedule> {
    public static final RoomSchedule dao = new RoomSchedule();

    public List<RoomSchedule> getDurationEventsByRoomId(long roomId, String start, String end) {
        String sqlStr = "select x.* from room_schedule x where x.roomid= ? and x.start_date>= ? and x.start_date< ?";
        return dao.find(sqlStr, roomId, start, end);
    }

    public boolean isLegalEvent(long start, long end, long roomId, long id) {
        String sql1 = "select count(*) from room_schedule " + " where start_date>from_unixtime(?) and start_date<from_unixtime(?) and roomid = ? and id <> ?";
        String sql2 = "select count(*) from room_schedule " + " where end_date>from_unixtime(?) and end_date<from_unixtime(?) and roomid = ? and id <> ?";
        String sql3 = "select count(*) from room_schedule " + " where start_date<=from_unixtime(?) and end_date>=from_unixtime(?) and roomid = ? and id <> ?";

        Long n1 = Db.queryLong(sql1, start, end, roomId, id);
        Long n2 = Db.queryLong(sql2, start, end, roomId, id);
        Long n3 = Db.queryLong(sql3, start, end, roomId, id);

        System.out.println(n1 + "-" + n2 + "-" + n3);
        return (n1 + n2 + n3) == 0 ? true : false;
    }

}
