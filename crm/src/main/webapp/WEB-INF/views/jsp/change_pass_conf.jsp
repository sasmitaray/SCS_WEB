<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.sales.crm.model.Beat"%>
<%@ page import="com.sales.crm.model.TrimmedCustomer"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">

<head>
<title>Create User</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
<link href="<%=request.getContextPath()%>/resources/css/bootstrap-datepicker.css" rel="stylesheet">
<script	src="<%=request.getContextPath()%>/resources/js/bootstrap-datepicker.js"></script>

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

.form_submit {
	margin-top: 14px;
	text-align: right;
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
		<!-- Links -->
		<div class="container">
		<nav class="navbar navbar-inverse">
			<div class="container-fluid">
				<div class="navbar-header">
					<a class="navbar-brand" href="#"></a>
				</div>
				<ul class="nav navbar-nav navbar-right ">
					<li class="dropdown">
        				<a class="dropdown-toggle" data-toggle="dropdown" href="#"><%= (String)session.getAttribute("userFullName") %> <span class="glyphicon glyphicon-user"></span></a>
	      				<ul class="dropdown-menu">
				          	<li><a href="<%=request.getContextPath()%>/logout">logout</a></li>
	      				</ul>
      				</li>	
					
				</ul>
			</div>
		</nav>
		<div class="row top-height">
			<div class="col-md-8 ">
					<br>
					<br>
					<c:choose>
    					<c:when test="${fn:length(msg) gt 0}">
       						<div class="alert alert-danger">
	 						 <strong>Error!</strong><br> ${msg}
						</div>
    					</c:when>    
    					<c:otherwise>
        					<div class="alert alert-success">
	 						 <strong>Success!</strong><br> Password has been successfully changed. Please log out and login again with new password.
						</div>
    					</c:otherwise>
					</c:choose>
			</div>
		</div>
	</div>
	</body>
</html>
