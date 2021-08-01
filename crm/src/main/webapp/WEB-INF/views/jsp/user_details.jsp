<%@page import="com.sales.crm.model.SalesExecutive"%>
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

.modal-custom-footer {
    padding: 15px;
    text-align: center;
    border-top: 1px solid #e5e5e5;
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
				<% if(resourcePermIDs.contains(ResourcePermissionEnum.USER_UPDATE.getResourcePermissionID())) { %>
					<button type="submit" class="btn btn-primary"
						onclick="location.href='${contextPath}/web/userWeb/editUserForm/${user.userID}';">
						Modify User</button>
				<% } %>		
				
				<% if(resourcePermIDs.contains(ResourcePermissionEnum.USER_DELETE.getResourcePermissionID())) { %>
					<button type="submit" class="btn btn-primary" id="deleteButton">
						Delete User</button>	
				<% } %>		
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
						<c:if test="${user.statusID == 1}">
   							<td>New</td>
						</c:if>
						<c:if test="${user.statusID == 2}">
   							<td>Active</td>
						</c:if>
						<c:if test="${user.statusID == 3}">
   							<td>Suspended</td>
						</c:if>
					</div>
				</fieldset>

				<fieldset>
					<legend>Role Details</legend>
					<div class="form-group">
						<label>Roles : </label>
						<%
							String values = "";
							boolean isSalesExec = false;
						%>
						<c:forEach var="role" items="${user.roles}">
							<%
								if(((Role) pageContext.getAttribute("role")).getRoleID() == 2){
									isSalesExec = true;
								}
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
					<% if(isSalesExec) { %>
						<input type="hidden" value="true" id="salesex"/>
						<% if(request.getAttribute("beats") != null && ((Boolean)request.getAttribute("beats")).booleanValue() == true) { %>
							<input type="hidden" value="true" id="beats"/>
						<% } %>
						
						<% if(request.getAttribute("customers") != null && ((Boolean)request.getAttribute("customers")).booleanValue() == true) { %>	
							<input type="hidden" value="true" id="customers"/>
						<% } %>
					<% } %>	
				</fieldset>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$('#deleteButton').click(function() {
		var message1 = "";
		var message2 = "";
		var status = false;
		if($('#beats').val() == "true"){
			status = true;
			message1 = "The Sales Executive is assigned to a beat, so can't be removed now. Please de-associate the beat from sales executive and then try to delete."	
		}
		if($('#customers').val() == "true"){
			status = true;
			message2 = "This Sales Executive is assigned to one or more customers, so can't be removed now. Please de-associate the sales executive from customers and then try to delete."	
		}
		
		if(status == true){
			$('#salesEX').text(message1);
			$('#salesEX2').text(message2);
			$('#confirm-submit').modal('show'); 
		}else{
			$('#confirm').modal('show'); 
	    }
	});

</script>

<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<b>Warning !</b>
			</div>
			<div class="modal-body"><span id="salesEX"></span><br><span id="salesEX2"></span></div>
			<div class="modal-custom-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirm" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<b>Confirm removal of user.</b>
			</div>
			<div class="modal-body">Are you sure you want to remove the
				user, <span><b>${ user.name }</b></span> ?</div>
			<div class="modal-custom-footer">
				<button type="submit" id="modalSubmit" class="btn btn-primary">Confirm</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
				<script type="text/javascript">
						$('#modalSubmit').click(function(){
							window.location.href = "${contextPath}/web/userWeb/delete/${user.userID}"
						});
					</script>
			</div>
		</div>
	</div>
</div>
</html>
