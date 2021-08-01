<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">

<head>
	<title>Change Password</title>
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
					action="${contextPath}/web/userWeb/updatePassword" id="passForm">
					<fieldset>
						<legend>Change Password</legend>
						<div class="form-group" >
							<label>User Name</label>
							<form:input name="userName" cssClass="form-control"
								path="userName" value="${ user.userName }" disabled="true"/>
						</div>
						<div class="form-group required" >
							<label class='control-label'>Existing Password</label>
							<form:password name="" cssClass="form-control"
								path="" value=""  id="passwd"/>
							<div id="msg"></div>	
						</div>
						<div class="form-group required" >
							<label class='control-label'>New Password</label>
							<form:password name="" cssClass="form-control"
								path="" value="" id="newpasswd"/>
						</div>
						<div class="form-group required" >
							<label class='control-label'>Confirm new password</label>
							<form:password name="newPassword" cssClass="form-control" path="newPassword"
								value="" id = "newpasswd1"/>
							<div id="msg1"></div>
						</div>
						
					</fieldset>
					<form:hidden path="userID" name="userID" value="${ user.userID }" />
					<input type="hidden" id="pass" value="${ user.password }">
					<div class="form_submit">
						<button type="submit" class="btn btn-primary" id="submitBtn">Submit</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
	<script type="text/javascript">
	$(document).ready(function() {
   		$("#passwd").prop('required',true);
	});
	
	$(document).ready(function() {
   		$("#newpasswd").prop('required',true);
	});
	
	$(document).ready(function() {
   		$("#newpasswd1").prop('required',true);
	});
	
	$(document).ready(function() {
		$('#passwd').blur(function() {
			if($('#passwd').val() != $('#pass').val()){
				$('#msg').empty();
				$("<font color=\"red\">Existing password entered is not valid.</font>").appendTo('#msg');
			}else{
				$('#msg').empty();
			}
		});
	});
	$(document).ready(function() {
		$('#newpasswd1').blur(function() {
			if($('#newpasswd').val() != $('#newpasswd1').val()){
				$('#msg1').empty();
				$("<font color=\"red\">New Passwords do not match.</font>").appendTo('#msg1');
			}else{
				$('#msg1').empty();
			}
			});
		});
	
	$(document).ready(function() {
		$('#submitBtn').click(function(event) {
			if(($('#passwd').val() != $('#pass').val()) || ($('#newpasswd').val() != $('#newpasswd1').val())){
				console.log("Inside if");
				event.preventDefault();
			}else{
				console.log("Inside else");
				$("#passForm").submit();
			}
		});
	});
	
	</script>
</html>
