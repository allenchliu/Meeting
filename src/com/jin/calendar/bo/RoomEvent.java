package com.jin.calendar.bo;

import java.util.Date;

public class RoomEvent extends Event {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String roomName;
    // private String email;

    protected int id;
    protected String text;
    protected Date start_date;
    protected Date end_date;
    protected String color;
    // protected String backgroundColor;
    // protected boolean editable;

    // custom
    // protected int isExpire;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    // public boolean isEditable() {
    // return editable;
    // }
    //
    // public void setEditable(boolean editable) {
    // this.editable = editable;
    // }
    //
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    //
    // public String getBackgroundColor() {
    // return backgroundColor;
    // }
    //
    // public void setBackgroundColor(String backgroundColor) {
    // this.backgroundColor = backgroundColor;
    // }
    //
    // public int getIsExpire() {
    // return isExpire;
    // }
    //
    // public void setIsExpire(int isExpire) {
    // this.isExpire = isExpire;
    // }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    // public String getEmail() {
    // return email;
    // }
    //
    // public void setEmail(String email) {
    // this.email = email;
    // }

    public RoomEvent() {
        super();
    }

    public RoomEvent(int id, String title, Date start_date, Date end_date, String userName, String roomName, String email, boolean editable) {
        super();
        this.id = id;
        this.text = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.userName = userName;
        this.roomName = roomName;
        // this.email = email;
        // this.editable = editable;
    }
}
