<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">

<head>
	<title>Edit User</title>
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

.side_nav_btns {
	margin-top: 10px;
}

}
.top-height {
	margin-top: 2%;
}

.side_nav_btns a {
	text-decoration: none;
	background: #337ab7;
	padding: 11px;
	border-radius: 12px;
	color: #ffffff;
	margin-top: 12px;
}

.form_submit {
	margin-top: 14px;
	text-align: right;
}

.form-group.required .control-label:after { 
   content:"*";
   color:red;
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
		<div class="row top-height">
			<div class="col-md-8 ">
				<h2>Edit User</h2>
				<form:form modelAttribute="user" method="post"
					action="${contextPath}/web/userWeb/update">
					<fieldset>
						<legend>User Details</legend>
						<div class="form-group required" >
							<label class='control-label'>First Name</label>
							<form:input name="firstName" cssClass="form-control"
								path="firstName" value="${ user.firstName }" />
						</div>
						<div class="form-group">
							<label>Last Name</label>
							<form:input name="lastName" cssClass="form-control"
								path="lastName" value="${ user.lastName }" />
						</div>
						<div class="form-group">
							<label>Description</label>
							<form:input name="description" cssClass="form-control"
								path="description" value="${ user.description }" />
						</div>
						<div class="form-group required" >
							<label class='control-label'>Email ID</label>
							<form:input name="emailID" cssClass="form-control" path="emailID"
								value="${ user.emailID }" disabled="true"/>
						</div>
						<div class="form-group">
							<label>Mobile Number</label>
							<form:input name="mobileNo" cssClass="form-control"
								path="mobileNo" value="${ user.mobileNo }" />
						</div>
					</fieldset>

					<fieldset>
						<legend>Role Details</legend>
						<div class="form-group">
							<label>Roles</label>
							<form:select path="roleIDs" cssClass="form-control"
								multiple="true">
								<form:options items="${roles}" itemValue="roleID"
									itemLabel="description" />
							</form:select>
						</div>
					</fieldset>
					<form:hidden path="statusID" name="statusID" value="${ user.statusID }" />
					<form:hidden path="userID" name="userID" value="${ user.userID }" />
					<form:hidden path="userName" name="userName" value="${ user.userName }" />
					<form:hidden path="password" name="password" value="${ user.password }" />
					<form:hidden path="emailID" name="emailID" value="${ user.emailID }" />
					<div>
						<fmt:formatDate value="${ user.dateCreated }" type="date"
								pattern="dd-MM-yyyy" var="createdDate" />
						<form:hidden path="dateCreated" value="${createdDate}"/>
					</div>
					<div>
						<fmt:formatDate value="${ user.dateModified }" type="date"
								pattern="dd-MM-yyyy" var="dateModified" />
						<form:hidden path="dateModified" value="${dateModified}"/>
					</div>
					<form:hidden path="tenantID" value="${ user.tenantID }"/>
					<form:hidden path="code" value="${ user.code }"/>
					<div class="form_submit">
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
	<script type="text/javascript">
		$(document).ready(function() {
	   		$("#name").prop('required',true);
		});
	</script>
</html>
