package com.jin.calendar.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class RoomSchedule extends Model<RoomSchedule> {
    public static final RoomSchedule dao = new RoomSchedule();

    public List<RoomSchedule> getDurationEventsByRoomId(long roomId, String start, String end) {
        String sqlStr = "select x.*, z.name roomname from room_schedule x left join room z on x.roomid=z.id "
                + "where x.roomid= ? and x.start_date>= ? and x.start_date< ?";
        return dao.find(sqlStr, roomId, start, end);
    }

    public List<RoomSchedule> getDurationEventsByRoomId(long roomId, long start, long end) {
        String sqlStr = "select x.*,y.name username,y.email, z.name roomname from room_schedule x left join user y on x.userid = y.id left join room z on x.roomid=z.id "
                + " where x.roomid= ? and x.start>=from_unixtime(?) and x.start<from_unixtime(?)";
        return dao.find(sqlStr, roomId, start, end);
    }

    public boolean isLegalEvent(long start, long end, long roomId, long id) {
        String sql1 = "select count(*) from room_schedule " + " where start>from_unixtime(?) and start<from_unixtime(?) and roomid = ? and id <> ?";
        String sql2 = "select count(*) from room_schedule " + " where end>from_unixtime(?) and end<from_unixtime(?) and roomid = ? and id <> ?";
        String sql3 = "select count(*) from room_schedule " + " where start<=from_unixtime(?) and end>=from_unixtime(?) and roomid = ? and id <> ?";

        Long n1 = Db.queryLong(sql1, start, end, roomId, id);
        Long n2 = Db.queryLong(sql2, start, end, roomId, id);
        Long n3 = Db.queryLong(sql3, start, end, roomId, id);

        System.out.println(n1 + "-" + n2 + "-" + n3);
        return (n1 + n2 + n3) == 0 ? true : false;
    }

    public List<RoomSchedule> getTodayEvent() {
        String sqlStr = "select x.*,y.name username,y.tel,y.email, z.name roomname from room_schedule x " + " left join user y on x.userid = y.id "
                + " left join room z on x.roomid=z.id " + " where to_days(start)=to_days(now()) order by start asc";
        return dao.find(sqlStr);
    }

}
