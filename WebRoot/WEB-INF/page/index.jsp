<!doctype html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Meeting Room Scheduler</title>

<script src="dhx/codebase/dhtmlxscheduler.js" type="text/javascript"
	charset="utf-8"></script>
<script src="dhx/codebase/ext/dhtmlxscheduler_recurring.js"
	type="text/javascript" charset="utf-8"></script>
<script src="dhx/codebase/ext/dhtmlxscheduler_minical.js"
	type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="dhx/codebase/dhtmlxscheduler.css"
	type="text/css" media="screen" title="no title" charset="utf-8">
<script src="js/jquery-1.7.2.min.js" type="text/javascript"
	charset="utf-8"></script>

<style type="text/css" media="screen">
html, body {
	margin: 0;
	padding: 0;
	height: 100%;
	overflow: hidden;
}

.room {
	cursor: pointer;
	color: #ffffff;
	font-size: 13px;
	background-color: #0CA1FE;
	width: 100px;
	margin: 2px 12px;
	border: 1px solid #BCD1FE;
	float: left;
	height: 24px;
	text-align: center;
	line-height: 24px;
	border-radius: 2px;
	font-family: "Lucida Grande", "Lucida Sans Unicode", "Lucida Sans",
		Geneva, Verda
}

.dhx_cal_event .dhx_body {
	-webkit-transition: opacity 0.1s;
	transition: opacity 0.1s;
	opacity: 0.9;
}

.dhx_cal_event.event_mine div, .dhx_cal_event_line.event_mine {
	background-color: orange !important;
	border-color: #a36800 !important;
}
</style>

<script type="text/javascript" charset="utf-8">
	var roomId;
	var events;
	function init() {
		roomId = getCookie("meeting_room_id") ? getCookie("meeting_room_id")
				: 1;
		$("#roomId" + roomId).css("backgroundColor", "orange");
		scheduler.config.xml_date = "%Y-%m-%d %H:%i";
		scheduler.config.time_step = 15;
		scheduler.config.multi_day = false;
		scheduler.locale.labels.section_subject = "Subject";
		scheduler.config.first_hour = 9;
		scheduler.config.last_hour = 19;
		scheduler.config.limit_time_select = true;
		scheduler.config.details_on_dblclick = true;
		scheduler.config.details_on_create = true;
		scheduler.config.icons_select = [ "icon_details", "icon_delete" ];

		scheduler.ignore_week = function(date) {
			//if (date.getDay() == 6 || date.getDay() == 0) //hides Saturdays and Sundays
			//return true;
		};

		scheduler.config.occurrence_timestamp_in_utc = true;
		scheduler.config.include_end_by = true;
		scheduler.config.repeat_precise = true;

		scheduler.config.lightbox.sections = [ {
			name : "Your Name",
			height : 26,
			map_to : "text",
			type : "textarea",
			focus : true
		//}, {
		//	name : "recurring",
		//	type : "recurring",
		//	map_to : "rec_type",
		//	button : "recurring"
		}, {
			name : "Time Period",
			height : 72,
			type : "calendar_time",
			time_format : [ "%H:%i", "%m", "%d", "%Y" ],
			map_to : "auto"
		} ];

		scheduler.init('scheduler_here', new Date(), "week");
		reload();

		scheduler.attachEvent("onEventChanged", function(id, data) {
			data.roomId = roomId;
			$.getJSON("/update", data, function(msg) {
				if (!msg.isSuccess) {
					dhtmlx.message(msg.msg);
				} else {
					dhtmlx.message("Successfully updated");
					setCookie("meeting_user", data.text);
				}
				reload();
			});
		});

		scheduler.attachEvent("onEventAdded", function(id, data) {
			data.roomId = roomId;
			$.getJSON("/add", data, function(msg) {
				if (!msg.isSuccess) {
					dhtmlx.message(msg.msg);
					scheduler.deleteEvent(id);
				} else {
					dhtmlx.message("Successfully scheduled");
					setCookie("meeting_user", data.text);
					setCookie("user_ip", msg.msg);
				}
				reload();
			});
		});

		scheduler.attachEvent("onBeforeEventDelete", function(id, data) {
			data.roomId = roomId;
			$.getJSON("/delete", data, function(msg) {
				if (!msg.isSuccess) {
					dhtmlx.message(msg.msg);
					reload();
				} else {
					dhtmlx.message("Successfully deleted");
				}
			});
			return true;
		});

		scheduler.attachEvent("onEventCollision", function(ev, evs) {
			return true; //blocks all other events
		});

		scheduler.templates.event_class = function(start, end, event) {
			var css = "";
			if (getCookie("user_ip") == event.password)
				css += " event_mine";
			return css; // default return
		};
	}

	Date.prototype.format = function(format) {
		/*  
		 * eg:format="YYYY-MM-dd hh:mm:ss";  
		 */
		var o = {
			"Y+" : this.getFullYear(),
			"M+" : this.getMonth() + 1, //month  
			"d+" : this.getDate(), //day  
			"h+" : this.getHours(), //hour  
			"m+" : this.getMinutes(), //minute  
			"s+" : this.getSeconds(), //second  
			"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter  
			"S" : this.getMilliseconds()
		//millisecond  
		}

		if (/(Y+)/.test(format)) {
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		}

		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
			}
		}
		return format;
	}

	function reload() {
		var curr = new Date; // get current date
		var first = curr.getDate() - curr.getDay(); // First day is the day of the month - the day of the week
		var last = first + 60; // two months duration
		var firstday = new Date(curr.setDate(first));
		var lastday = new Date((new Date).setDate(last));
		//dhtmlx.message("Week: " + firstday + " to " + lastday);
		var sdatestr = firstday.format("YYYY-MM-dd 00:00:00");
		var edatestr = lastday.format("YYYY-MM-dd 23:59:59");

		var events = $.getJSON("/load", "roomId=" + roomId + "&start="
				+ sdatestr + "&end=" + edatestr + "", function(msg) {
			scheduler.clearAll();
			scheduler.parse(msg, "json");
			//scheduler.setCurrentView();
		});
	}

	function changeRoom(id) {
		$(".room").each(function(i) {
			if (this.id == id) {
				$(this).css("backgroundColor", "orange");
				roomId = $(this).attr("roomId");
				setCookie("meeting_room_id", roomId);
			} else {
				$(this).css("backgroundColor", "#0CA1FE");
			}
		});
		reload();
	}

	function getCookie(name) {
		var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
		if (arr = document.cookie.match(reg))
			return unescape(arr[2]);
		else
			return null;
	}
	function setCookie(name, value) {
		var Days = 30;
		var exp = new Date();
		exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
		document.cookie = name + "=" + escape(value) + ";expires="
				+ exp.toGMTString();
	}
</script>
</head>
<body onload="init();">
	<div class="room" id="roomId4" roomId="4" name="3F Conference"
		onclick="changeRoom(this.id)">3F Conference</div>
	<div class="room" id="roomId3" roomId="3" name="3F Pantry"
		onclick="changeRoom(this.id)">3F Pantry</div>
	<div class="room" id="roomId1" roomId="1" name="5F Pantry"
		onclick="changeRoom(this.id)">5F Pantry</div>
	<div class="room" id="roomId2" roomId="2" name="5F Interview"
		onclick="changeRoom(this.id)">5F Interview</div>
	<div id="scheduler_here" class="dhx_cal_container"
		style='width: 100%; height: 100%;'>
		<div class="dhx_cal_navline">
			<div class="dhx_cal_prev_button">&nbsp;</div>
			<div class="dhx_cal_next_button">&nbsp;</div>
			<div class="dhx_cal_today_button"></div>
			<div class="dhx_cal_date"></div>
			<div class="dhx_cal_tab" name="day_tab" style="right: 204px;"></div>
			<div class="dhx_cal_tab" name="week_tab" style="right: 140px;"></div>
			<div class="dhx_cal_tab" name="month_tab" style="right: 76px;"></div>
		</div>
		<div class="dhx_cal_header"></div>
		<div class="dhx_cal_data"></div>
	</div>
</body>
