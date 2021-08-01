<%@page import="com.sales.crm.model.ResourcePermission"%>
<%@page import="com.sales.crm.model.Permission"%>
<%@page import="com.sales.crm.model.Beat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">

<head>
<title>Page one</title>
<!-- Bootstrap Core CSS -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
<script
	src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap-datepicker.css"
	rel="stylesheet">
<script
	src="<%=request.getContextPath()%>/resources/js/bootstrap-datepicker.js"></script>

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
	width: 752;
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

.add_customer {
	text-align: right;
	margin-top: 31px;
}

.form-group.required .control-label:after {
	content: "*";
	color: red;
}

.customer_list {
	margin-bottom: 20px;
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
		<%@ include file="menus.jsp"%>
		<div class="row customer_list">
			<div class="col-md-4">
				<h2>Role Privileges</h2>
			</div>
			<div class="col-md-4 add_customer">
				<button type="submit" class="btn btn-primary"
					onclick="location.href='<%=request.getContextPath()%>/web/role/list';">Back to list of Roles</button>
				<% if(resourcePermIDs.contains(ResourcePermissionEnum.ROLE_EDIT_RESOURCE_PERMISSION.getResourcePermissionID())) { %>	
					<button type="submit" class="btn btn-primary"
						onclick="location.href='<%=request.getContextPath()%>/web/role/resource_permission/<%=Integer.valueOf(String.valueOf(request.getAttribute("roleID")))%>/editform';">Edit Permissions</button>
				<% } %>
			</div>
		</div>
		<c:forEach var="resPerm" items="${resPermMap}">
			<fieldset>
				<legend>${resPerm.key.name}</legend>
				<c:forEach var="resPerm" items="${resPerm.value}" varStatus="status">
					<div>
						<%
									if (((ResourcePermission) pageContext.getAttribute("resPerm")).isPresent()) {
								%>
						<input type="checkbox" name="resperm" checked="checked"
							value="${ resPerm.id }" class="bold" disabled />
						<%
									} else {
								%>
						<input type="checkbox" name="resperm" value="${ resPerm.id }"
							disabled />
						<%
									}
								%>
						<b>${resPerm.permission.name}</b> ( ${resPerm.permission.description} )
					</div>
				</c:forEach>
			</fieldset>
		</c:forEach>

	</div>
</body>
<script type="text/javascript">
	
</script>

</html>
