<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User Login</title>
<link href="/css/login.css" rel='stylesheet' />
<script type="text/javascript">
function switchSys(event){
	if(event.id=='order-food'){
		document.getElementById('book-room').checked=false;
	}else{
		document.getElementById('order-food').checked=false;
	}
}
</script>
</head>
<body>
	<div id="container">
		<form action="/login" method="post">
<!-- 			<div class="login">User Login</div> -->
			<div class="username-text">User Name</div>
			<div class="password-text">Password</div>
			<div class="username-field">
				<input type="text" name="username" />
			</div>
			<div class="password-field">
				<input type="password" name="password" />
			</div>
			<!-- 
			<input type="checkbox" name="pageType" id="order-food" checked="checked" onclick="switchSys(this)" value="orderFood" /><label
				for="order-food">Food</label>
			<input type="checkbox" name="pageType" id="book-room" onclick="switchSys(this)" value="bookRoom" /><label
				for="book-room" selected="">Meeting Room</label>
			 -->
			<input type="submit" name="submit" value="GO" />
			<div class="forgot-usr-pwd">${msg }</div>
		</form>
	</div>
</body>
</html>