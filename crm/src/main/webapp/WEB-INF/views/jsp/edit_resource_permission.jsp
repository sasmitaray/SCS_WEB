<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
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
	width: 750;
}

.customer_list {
	margin-bottom: 20px;
}

.add_customer {
	text-align: right;
	margin-top: 31px;
}

.form-group.required .control-label:after {
	content: "*";
	color: red;
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
		<div class="row top-height">
			<div class="col-md-4 ">
				<h2>Update/Assign Privileges</h2>
			</div>
			<div class="col-md-4 add_customer">
				<button type="submit" class="btn btn-primary" id="submitbtn">Submit</button>
			</div>
		</div>
		<form:form modelAttribute="resPermWebModel" method="post"
			action="${contextPath}/web/role/resource_permission/save" id="respermform">
			<input type="checkbox" id="checkAll"> <b>Select All</b>
			<c:forEach var="resPerms" items="${resPermMap}">
				<fieldset>
					<legend>${resPerms.key.name}</legend>
					<c:forEach var="resPerm" items="${resPerms.value}">
						<div>
							<% if (((ResourcePermission) pageContext.getAttribute("resPerm")).isPresent()) { %>
								<form:checkbox path="resourcePermIDList" name="resperm" class="checkBoxClass" id="${resPerms.key.resourceKey}-${resPerm.permission.permissionKey}" value="${ resPerm.id }" checked="checked" />
							<%} else { %>
								<form:checkbox path="resourcePermIDList" name="resperm" class="checkBoxClass" id="${resPerms.key.resourceKey}-${resPerm.permission.permissionKey}" value="${ resPerm.id }" />
							<% } %>
							<b>${resPerm.permission.name}</b> (
							${resPerm.permission.description} )
						</div>
					</c:forEach>
				</fieldset>
			</c:forEach>
			<form:hidden path="tenantID" value="${ tenantID }" />
			<form:hidden path="roleID" value="${ roleID }" />
			<div class="form_submit">
				<button type="submit" class="btn btn-primary" >Submit</button>
			</div>
		</form:form>
	</div>
</body>
<script type="text/javascript">
	$( document ).ready(function() {
	  	var atLeastOneIsChecked = $('input[type="checkbox"]:checked').length > 0;
		if(atLeastOneIsChecked == false){
			$('button').prop('disabled', true);
		}
		
		if($('.checkBoxClass:checkbox:checked')){
			$("#checkAll").prop('checked', true);	
		}
	});
	
	$('input[type="checkbox"]').change(function() {
		//If READ is checked, then check LIST
		var id = $(this).attr("id");
		if(id.indexOf("-READ") != -1){
			if(this.checked){
				var listID = id.substring(0, id.indexOf("-")).concat("-LIST");
				$('#'+ listID).prop('checked', $(this).prop('checked'));
			}
		}
		
		//Disable/enable submit button
		var atLeastOneIsChecked = $('input[type="checkbox"]:checked').length > 0;
		if(atLeastOneIsChecked == false){
			$('button').prop('disabled', true);
		}else{
			$('button').prop('disabled', false);
		}
	}); 
	
	//Checkall checkbox
	$("#checkAll").click(function () {
	    $(".checkBoxClass").prop('checked', $(this).prop('checked'));
	});
	
	$('#submitbtn').click(function(){
	     /* when the submit button in the modal is clicked, submit the form */
	   $('#respermform').submit();
	});
</script>

</html>
