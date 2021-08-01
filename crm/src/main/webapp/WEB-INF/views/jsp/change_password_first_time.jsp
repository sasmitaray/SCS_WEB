<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">

<head>
	<title>Change Password</title>
	<!-- Bootstrap Core CSS -->
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" />
	<script src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

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
		<nav class="navbar navbar-inverse">
			<div class="container-fluid">
				<div class="navbar-header">
					<a class="navbar-brand" href="#"></a>
				</div>
				<ul class="nav navbar-nav navbar-right ">
					<li class="dropdown">
        				<a class="dropdown-toggle" data-toggle="dropdown" href="#"><%= (String)session.getAttribute("userFullName") %> <span class="glyphicon glyphicon-user"></span></a>
	      				<ul class="dropdown-menu">
				          	<li><a href="${contextPath}/logout">logout</a></li>
	      				</ul>
      				</li>	
					
				</ul>
			</div>
		</nav>
		<div class="row top-height">
			<div class="col-md-8 ">
				<h2>Change Password</h2>
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
								path="" value=""  id="passwd" required="required"/>
							<div id="msg"></div>	
						</div>
						<div class="form-group required" >
							<label class='control-label'>New Password</label>
							<form:password name="" cssClass="form-control"
								path="" value="" id="newpasswd" required="required"/>
						</div>
						<div class="form-group required" >
							<label class='control-label'>Confirm new password</label>
							<form:password name="newPassword" cssClass="form-control" path="newPassword"
								value="" id = "newpasswd1" required="required"/>
							<div id="msg1"></div>
						</div>
					</fieldset>	
					<fieldset>
					<legend>Security Questions</legend>
						<div class="form-group required">
							<label class='control-label'>Security Question</label>
							<form:select path="securityQuestions" cssClass="form-control" multiple="false" id="secQ1">
								<form:option value="" label="--- Select ---" />
								<form:options items="${secQues}" itemValue="id"
									itemLabel="question" required="required"/>
							</form:select>
						</div>
						
						<div class="form-group required">
							<label class='control-label'>Answer</label>
							<form:input name="answer1" cssClass="form-control"
								path="secQuestionAnsws[0]" value="" required="required" id="ans1"/>
						</div>
						
						<div class="form-group required">
							<label class='control-label'>Security Question</label>
							<form:select path="securityQuestions" cssClass="form-control" multiple="false" id="secQ2">
								<form:option value="" label="--- Select ---" />
								<form:options items="${secQues}" itemValue="id"
									itemLabel="question" required="required"/>
							</form:select>
							<div id="msg2"></div>
						</div>
						
						<div class="form-group required">
							<label class='control-label'>Answer</label>
							<form:input name="answer2" cssClass="form-control"
								path="secQuestionAnsws[1]" value="" required="required" id="ans2"/>
						</div>
						
					</fieldset>
					<form:hidden path="userID" name="userID" value="${ user.userID }" />
					<input type="hidden" id="pass" value="${ user.password }">
					<form:hidden path="loggedIn" name="loggedIn" value="${ user.loggedIn }"/>
					<form:hidden name="tenantID" path="tenantID" value="${ user.tenantID }" />
					<div>
						<fmt:formatDate value="${ user.dateCreated }" type="date"
								pattern="dd-MM-yyyy" var="createdDate" />
							<form:hidden path="dateCreated" value="${createdDate}" />
					</div>
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
   		
		$("#newpasswd").prop('required',true);
		
		$("#newpasswd1").prop('required',true);
		
		$("#secQ1").prop('required',true);
		
		$("#secQ2").prop('required',true);
		
		$("#ans1").prop('required',true);
		
		$("#ans2").prop('required',true);
		
		$('#passwd').blur(function() {
			if($('#passwd').val() != $('#pass').val()){
				$('#msg').empty();
				$("<font color=\"red\">Existing password entered is not valid.</font>").appendTo('#msg');
			}else{
				$('#msg').empty();
			}
		});
		
		$('#newpasswd1').blur(function() {
			if($('#newpasswd').val() != $('#newpasswd1').val()){
				$('#msg1').empty();
				$("<font color=\"red\">New Passwords do not match.</font>").appendTo('#msg1');
			}else{
				$('#msg1').empty();
			}
		});
		
		//$('#submitBtn').click(function(event) {
		//		$("#passForm").submit();
		//});
		
		$('#secQ2').change(function() {
			var val1 = $("#secQ1").prop('value');
			var val2 = $("#secQ2").prop('value');
			if(val1 == val2){
				$('#msg2').empty();
				$("<font color=\"red\">Please select a different security question.</font>").appendTo('#msg2');
			}else{
				$('#msg2').empty();
			}
		});
		
		$('#secQ1').change(function() {
			var val1 = $("#secQ1").prop('value');
			var val2 = $("#secQ2").prop('value');
			if(val1 == val2){
				$('#msg2').empty();
				$("<font color=\"red\">Please select a different security question.</font>").appendTo('#msg2');
			}else{
				$('#msg2').empty();
			}
		});
		
		
	});
	
	</script>
</html>
