<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Meeting Room Reservation</title>
<link href='/css/style.css' rel='stylesheet' />
<link href='/css/jquery-ui.min.css' rel='stylesheet' />
<link href='/css/fullcalendar.css' rel='stylesheet' />
<link href='/css/fullcalendar.print.css' rel='stylesheet' media='print' />
<link href='/css/jquery.qtip.min.css' rel='stylesheet'/>
<script src='/js/jquery.min.js'></script>
<script src='/js/jquery-ui.custom.min.js'></script>
<script src='/js/jquery.qtip.min.js'></script>
<script src='/js/fullcalendar.min.js'></script>
<script>
var roomId;
$(document).ready(function() {
	
	roomId = $("#room1").attr("roomId");
	$("#room1").css("backgroundColor", "#003399");

	var datetime = $.fullCalendar.parseDate("${ServerTime}");

	/* initialize the calendar
	-----------------------------------------------------------------*/
	
	$('#calendar').fullCalendar({
		theme: true,
		year: datetime.getFullYear(),
		month: datetime.getMonth(),
		date: datetime.getDate(),
		header: {
			left: '',
			center: 'title',
			right: 'prev,next today'
		},
		defaultView:'agendaWeek',
		firstDay:1,
		allDaySlot: false,
		allDayDefault: false,
		buttonText: {
			today: 'Today',
			month: 'Month',
			week: 'Week',
			day: 'Day'
		},
		monthNames: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
		monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
		dayNames: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
		dayNamesShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
		timeFormat: {
			agenda: 'h:mmtt{ - h:mmtt}',
			'': 'h(:mm)tt'
		},
		columnFormat: {
			month: 'ddd',
			week: 'MM/dd ddd',
			day: 'ddd'
		},
		titleFormat: {
			month: 'yyyy 年 MMM 月',
			week: "yyyy.MM.dd{ '&#8212;' yyyy.MM.dd}",
			day: 'ddd'
		},
		events: function( start, end, callback ) {
			$.getJSON("/roomSchedule/getDurationEvent", { roomId: roomId,start:Math.round(start.getTime()/1000), end:Math.round(end.getTime()/1000)}, function(events){
				callback(events);
			});
		},
		snapMinutes: 5,
		eventResize: function( event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ) {
			$.getJSON("/roomSchedule/updateRoomEvent",{id: event.id, start: event.start.getTime(), end: event.end.getTime(), roomId: roomId}, function(data){
				if(!data.isSuccess){
					alert(data.msg);
					revertFunc();
				}
			});
		},
		eventDrop: function( event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view ) {
			$.getJSON("/roomSchedule/updateRoomEvent",{id: event.id, start: event.start.getTime(), end: event.end.getTime(), roomId: roomId}, function(data){
				if(!data.isSuccess){
					alert(data.msg);
					revertFunc();
				}
			});
		},
		selectable: true,
		selectHelper: true,
		select: function(start, end, allDay) {
			if(start.getTime()<=new Date().getTime()){
				alert("Start time is already passed...");
			}else{
				var title = prompt('Meeting Subject: ');
				if (title) {
					$.getJSON("/roomSchedule/addRoomEvent",{start: start.getTime(), end: end.getTime(), roomId: roomId, title: title}, function(data){
						if(data.isSuccess){
							$('#calendar').fullCalendar('refetchEvents');
						}else{
							alert(data.msg);
						}
					});
				}
			}
			$('#calendar').fullCalendar('unselect');

		},
		eventRender: function(event, element) {
// 			console.log(element.html());
			element.qtip({ 
				id: event.id,
				style: {
					classes: 'qtip-bootstrap qtip-shadow'
				},
				content: {
					title: event.roomName,
					text: getTipContent(event.userName, event.start, event.end, event.title, event.email).clone()
				},
				position: {
					my: 'right center',
					at: 'left center'
				}
			});
		},
		loading: function(bool) {
			if (bool) $('#loading').show();
			else $('#loading').hide();
		}
	});
	
	
});

function getEvents(start, end){
	$.getJSON("/roomSchedule/getDurationEvent", { roomId: roomId,start:Math.round(start.getTime()/1000), end:Math.round(end.getTime()/1000)}, function(events){
		  return events;
	});
}
function changeRoom(id){
	$(".external-event").each(function(i){
		if(this.id==id){
			$(this).css("backgroundColor", "#003399");
			roomId = $(this).attr("roomId");
		}else{
			$(this).css("backgroundColor", "#3366CC");
		}
	});
	$('#calendar').fullCalendar('refetchEvents');
}
function getTipContent(initiator, start, end, subject, contact){
	$("#initiator").text(initiator);
	$("#contact").text(contact);
	$("#tb").text($.fullCalendar.formatDate(start,'hh:mmtt')+" - "+$.fullCalendar.formatDate(end,'hh:mmtt'));
	$("#subject").text(subject);
	return $('#qTipContent');
}
</script>
</head>
<body>
	<div id='wrap'>

		<div id='external-events'>
			<h4>Meeting Rooms</h4>
			<c:forEach items="${roomList}" var="room" varStatus="status">
				<div class='external-event' id="room${status.count}" roomId="${room.id}" onclick="changeRoom(this.id)">${room.name}</div>
			</c:forEach>
		</div>

		<div id='calendar'></div>

		<div style='clear: both'></div>
	</div>
	<div id='loading'>loading...</div>
	<!-- 
	<div id='exit'><a href="/exit">退出</a></div>
	 -->
	<div id="qTipContent">
		<table>
			<tr>
				<td>Created By: </td>
				<td><span id="initiator"></span></td>
			</tr>
			<tr>
				<td>Contact: </td>
				<td><span id="contact"></span></td>
			</tr>
			<tr>
				<td>Time Slot: </td>
				<td><span id="tb"></span></td>
			</tr>
			<tr>
				<td>subject: </td>
				<td><span id="subject"></span></td>
			</tr>
		</table>
	</div>
</body>
</html>
