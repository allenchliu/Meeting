<!doctype html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title>Meeting Room Scheduler</title>

<script src="dhx/codebase/dhtmlxscheduler.js" type="text/javascript"
	charset="utf-8"></script>
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

.dhx_cal_event div.dhx_footer, .dhx_cal_event.past_event div.dhx_footer,
	.dhx_cal_event.event_english div.dhx_footer, .dhx_cal_event.event_math div.dhx_footer,
	.dhx_cal_event.event_science div.dhx_footer {
	background-color: transparent !important;
}

.dhx_cal_event .dhx_body {
	-webkit-transition: opacity 0.1s;
	transition: opacity 0.1s;
	opacity: 0.7;
}

.dhx_cal_event .dhx_title {
	line-height: 12px;
}

.dhx_cal_event_line:hover, .dhx_cal_event:hover .dhx_body,
	.dhx_cal_event.selected .dhx_body, .dhx_cal_event.dhx_cal_select_menu .dhx_body
	{
	opacity: 1;
}

.dhx_cal_event.event_math div, .dhx_cal_event_line.event_math {
	background-color: orange !important;
	border-color: #a36800 !important;
}

.dhx_cal_event_clear.event_math {
	color: orange !important;
}

.dhx_cal_event.event_science div, .dhx_cal_event_line.event_science {
	background-color: #36BD14 !important;
	border-color: #698490 !important;
}

.dhx_cal_event_clear.event_science {
	color: #36BD14 !important;
}

.dhx_cal_event.event_english div, .dhx_cal_event_line.event_english {
	background-color: #FC5BD5 !important;
	border-color: #839595 !important;
}

.dhx_cal_event_clear.event_english {
	color: #B82594 !important;
}
</style>

<script type="text/javascript" charset="utf-8">
	var roomId;
	var events;
	function init() {
		roomId = 1;
		$("#roomId1").css("backgroundColor", "#27A023");
		scheduler.config.xml_date = "%Y-%m-%d %H:%i";
		scheduler.config.time_step = 15;
		scheduler.config.multi_day = false;
		scheduler.locale.labels.section_subject = "Subject";
		scheduler.config.first_hour = 9;
		scheduler.config.last_hour = 19;
		scheduler.config.limit_time_select = true;
		scheduler.config.details_on_dblclick = true;
		scheduler.config.details_on_create = true;
		// 0 refers to Sunday, 6 - to Saturday
		scheduler.ignore_week = function(date) {
			if (date.getDay() == 6 || date.getDay() == 0) //hides Saturdays and Sundays
				return true;
		};

		scheduler.config.lightbox.sections = [ {
			name : "Meeting Name",
			height : 20,
			map_to : "text",
			type : "textarea",
			focus : true
		}, {
			name : "User",
			height : 20,
			map_to : "userName",
			type : "textarea"
		}, {
			name : "Password",
			height : 20,
			map_to : "password",
			type : "textarea"
		}, {
			name : "Time Period",
			height : 72,
			type : "time",
			map_to : "auto"
		} ];

		scheduler.init('scheduler_here', new Date(), "week");
		reload();

		scheduler.attachEvent("onEventChanged", function(id, data) {
			data.roomId = roomId;
			$.getJSON("/update", data, function(msg) {
				if (!msg.isSuccess) {
					dhtmlx.message(msg.msg);
					reload();
				}
			});
			dhtmlx.message("Successfully updated");
		});

		scheduler.attachEvent("onEventAdded", function(id, data) {
			data.roomId = roomId;
			$.getJSON("/add", data, function(msg) {
				if (!msg.isSuccess) {
					dhtmlx.message(msg.msg);
					scheduler.deleteEvent(id);
					return false;
				}
			});
			dhtmlx.message("Successfully scheduled");
			return true;
		});

		scheduler.attachEvent("onBeforeEventDelete", function(id, data) {
			data.roomId = roomId;
			$.getJSON("/delete", data, function(msg) {
				if (!msg.isSuccess) {
					dhtmlx.message(msg.msg);
					return false;
				}
			});
			dhtmlx.message("Successfully deleted");
			return true;
		});

		scheduler.attachEvent("onEventCollision", function(ev, evs) {
			return true; //blocks all other events
		});

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
		var last = first + 6; // last day is the first day + 6
		var firstday = new Date(curr.setDate(first));
		var lastday = new Date(curr.setDate(last));
		var sdatestr = firstday.format("YYYY-MM-dd hh:mm:ss");
		var edatestr = lastday.format("YYYY-MM-dd hh:mm:ss");
		;

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
				$(this).css("backgroundColor", "#27A023");
				roomId = $(this).attr("roomId");
			} else {
				$(this).css("backgroundColor", "#0CA1FE");
			}
		});
		reload();
	}
</script>
</head>
<body onload="init();">
	<div class="room" id="roomId3" roomId="3" name="3F Pantry"
		onclick="changeRoom(this.id)">3F Pantry</div>
	<div class="room" id="roomId4" roomId="4" name="3F Conference"
		onclick="changeRoom(this.id)">3F Conference</div>
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
		</div>
		<div class="dhx_cal_header"></div>
		<div class="dhx_cal_data"></div>
	</div>
</body>
