<%@page import="com.sales.crm.model.Role"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.sales.crm.model.Role"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en">

<head>
	<title>User Details</title>
	<!-- Bootstrap Core CSS -->
	<meta charset="utf-8">	
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" />
	<script src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>

<style>
.dpHeaderWrap {
	position: relative;
	width: auto;
	height: 80px;
	background: #fff;
	border-style: solid;
	border-bottom-style: groove;
	border-top-style: none;
	border-left-style: none;
	border-right-style: none;
	margin: 10px;
}

fieldset {
	border: 1px solid grey;
	padding: 10px;
	border-radius: 5px;
}

legend {
	width: auto !important;
	border-bottom: 0px !important;
}

}
.top-height {
	margin-top: 2%;
}

.customer_list {
	margin-bottom: 20px;
}

.add_customer {
	text-align: right;
	margin-top: 31px;
}

.side_nav_btns {
	margin-top: 10px;
}

.side_nav_btns a {
	text-decoration: none;
	background: #337ab7;
	padding: 11px;
	border-radius: 12px;
	color: #ffffff;
	margin-top: 12px;
}
</style>
</head>

<body>
	<!-- Header -->
	<header class="dpHeaderWrap">
		<div class="text-center">Header part</div>
	</header>
	<!-- Header -->
	<div class="container">
		<%@ include file="menus.jsp" %>
		<div class="row customer_list">
			<div class="col-md-4">
				<h2>User Details</h2>
			</div>
			<div class="col-md-4 add_customer">
				<button type="submit" class="btn btn-primary"
					onclick="location.href='<%=request.getContextPath()%>/web/userWeb/editUserForm/${user.userID}';">
					Modify Details</button>
				
				<button type="submit" class="btn btn-primary"
					onclick="location.href='<%=request.getContextPath()%>/web/userWeb/changePassForm';">
					Change Password</button>	
			</div>
		</div>
		<div class="row top-height">
			<div class="col-md-8 ">
				<fieldset>
					<legend>User Details</legend>
					<div class="form-group">
						<label>First Name : </label> <span>${ user.firstName }</span>
					</div>
					<div class="form-group">
						<label>Last Name : </label> <span>${ user.lastName }</span>
					</div>
					<div class="form-group">
						<label>Description : </label> <span>${ user.description }</span>
					</div>
					<div class="form-group">
						<label>Email ID : </label> <span>${ user.emailID }</span>
					</div>
					<div class="form-group">
						<label>Mobile Number : </label> <span>${ user.mobileNo }</span>
					</div>
				</fieldset>

				<fieldset>
					<legend>Login Details</legend>
					<div class="form-group">
						<label>User Name : </label> <span>${ user.userName }</span>
					</div>
					<div class="form-group">
						<label>Status : </label>
						<c:if test="${user.statusID == 2}">
							<span>Active</span>
						</c:if>
					</div>
				</fieldset>

				<fieldset>
					<legend>Role Details</legend>
					<div class="form-group">
						<label>Roles : </label>
						<%
							String values = "";
						%>
						<c:forEach var="role" items="${user.roles}">
							<%
								if (values.isEmpty()) {
										values = values + ((Role) pageContext.getAttribute("role")).getDescription();
									} else {
										values = values + " ,";
										values = values + ((Role) pageContext.getAttribute("role")).getDescription();
									}
							%>
						</c:forEach>
						<c:if test="${fn:length(user.roles) gt 0}">
							<span><%=values%></span>
						</c:if>
						<c:if test="${fn:length(user.roles) eq 0}">
							<span>None</span>
						</c:if>
					</div>
				</fieldset>
			</div>
		</div>
	</div>
</body>
</html>
