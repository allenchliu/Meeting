<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form id="pagerForm" method="post" action="/user/getPageList">
	<input type="hidden" name="pageNum" value="1" />
	<input type="hidden" name="numPerPage" value="${page.pageSize}" />
</form>


<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="admin/user_add.jsp" target="dialog" mask=true minable=false ref="dlg_user_add" title="新增" width="550" height="300"><span>新增</span></a></li>
			<li><a class="delete" href="user/del/{user_id}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="75">
		<thead>
			<tr>
				<th style="display: none;"></th>
				<th width="100">真实姓名</th>
				<th width="150">登录名</th>
				<th width="80">TEL</th>
				<th width="80">e-mail</th>
				<th width="80">创建日期</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="item">
				<tr target="user_id" rel="${item.id}">
					<td style="display: none;">${item.id}</td>
					<td>${item.name}</td>
					<td>${item.loginName}</td>
					<td>${item.tel}</td>
					<td>${item.email}</td>
					<td>${item.create_date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="panelBar">
		<div class="pages">
			<span>显示</span>
			<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})" >
				<c:choose>
					<c:when test="${page.pageSize == 10}">
						<option value="10" selected="selected">10</option>
					</c:when>
					<c:otherwise>
						<option value="10">10</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${page.pageSize == 50}">
						<option value="50" selected="selected">50</option>
					</c:when>
					<c:otherwise>
						<option value="50">50</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${page.pageSize == 100}">
						<option value="100" selected="selected">100</option>
					</c:when>
					<c:otherwise>
						<option value="100">100</option>
					</c:otherwise>
				</c:choose>
			</select>
			<span>条，共${page.totalRow}条</span>
		</div>
		
		<div class="pagination" targetType="navTab" totalCount="${page.totalRow}" numPerPage="${page.pageSize}" pageNumShown="10" currentPage="${page.pageNumber}"></div>

	</div>
</div>
