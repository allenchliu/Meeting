package com.jin.calendar.orderfood.bo;

import java.util.Date;

public class RoomEvent extends Event {

	private static final long serialVersionUID = 1L;

	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public RoomEvent() {
		super();
	}
	
	public RoomEvent(int id, String title, Date start, Date end, String userName, boolean editable) {
		super();
		this.id=id;
		this.title=title;
		this.start=start;
		this.end=end;
		this.userName = userName;
		this.editable = editable;
	}
}
