<%@page import="com.sales.crm.model.Beat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">

<head>
	<title>Create Customer</title>
	<!-- Bootstrap Core CSS -->
	<meta charset="utf-8">
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
				<h2>Add New Customer</h2>
				<form:form modelAttribute="customer" method="post"
					action="${contextPath}/web/customerWeb/save">
					<fieldset>
						<legend>Customer Details</legend>
						<div class="form-group required">
							<label class='control-label'>Name</label>
							<form:input name="name" cssClass="form-control" path="name" id="name"/>
						</div>
						<div class="form-group">
							<label>Description</label>
							<form:input name="description" cssClass="form-control"
								path="description" />
						</div>
					</fieldset>

					<fieldset>
						<legend>Customer Main Address</legend>
						<form:hidden name="addrressType" value="1"
							path="address[0].addrressType" />
						<div class="form-group required" >
							<label class='control-label'>Contact Person</label>
							<form:input name="contactPerson" cssClass="form-control"
								path="address[0].contactPerson" id="main_cp"/>
						</div>
						<div class="form-group required" >
							<label class='control-label'>Address Line 1</label>
							<form:input name="addressLine1" cssClass="form-control"
								path="address[0].addressLine1" id="main_add1"/>
						</div>
						<div class="form-group">
							<label>Address Line 2</label>
							<form:input name="addressLine2" cssClass="form-control"
								path="address[0].addressLine2" />
						</div>
						<div class="form-group">
							<label>Street</label>
							<form:input name="street" cssClass="form-control"
								path="address[0].street" />
						</div>
						<div class="form-group required" >
							<label class='control-label'>City</label>
							<form:input name="city" cssClass="form-control"
								path="address[0].city" id="main_city"/>
						</div>
						<div class="form-group required" >
							<label class='control-label'>State</label>
							<form:input name="state" cssClass="form-control"
								path="address[0].state" id="main_state"/>
						</div>
						<div class="form-group required" >
							<label class='control-label'>Country</label>
							<form:input name="country" cssClass="form-control"
								path="address[0].country" id="main_cntry"/>
						</div>
						<div class="form-group required" >
							<label class='control-label'>Postal Code</label>
							<form:input name="postalCode" cssClass="form-control"
								path="address[0].postalCode" id="main_postCode"/>
						</div>
						<div class="form-group">
							<label>Phone Number</label>
							<form:input name="phoneNumber" cssClass="form-control"
								path="address[0].phoneNumber" />
						</div>
						<div class="form-group required">
							<label class='control-label'>Mobile Number(Primary)</label>
							<form:input name="mobileNumberPrimary" cssClass="form-control"
								path="address[0].mobileNumberPrimary" maxlength="10" id="main_mobPri"/>
						</div>
						<div class="form-group">
							<label>Mobile Number(Secondary)</label>
							<form:input name="mobileNumberSecondary" cssClass="form-control"
								path="address[0].mobileNumberSecondary" />
						</div>
					</fieldset>
					<fieldset>
						<legend>Customer Billing Address</legend>
						<form:hidden name="addrressType" value="2"
							path="address[1].addrressType" />
						<div class="form-group">
							<label>Contact Person</label>
							<form:input name="contactPerson" cssClass="form-control"
								path="address[1].contactPerson" />
						</div>
						<div class="form-group">
							<label>Address Line 1</label>
							<form:input name="addressLine1" cssClass="form-control"
								path="address[1].addressLine1" />
						</div>
						<div class="form-group">
							<label>Address Line 2</label>
							<form:input name="addressLine2" cssClass="form-control"
								path="address[1].addressLine2" />
						</div>
						<div class="form-group">
							<label>Street</label>
							<form:input name="street" cssClass="form-control"
								path="address[1].street" />
						</div>
						<div class="form-group">
							<label>City</label>
							<form:input name="city" cssClass="form-control"
								path="address[1].city" />
						</div>
						<div class="form-group">
							<label>State</label>
							<form:input name="state" cssClass="form-control"
								path="address[1].state" />
						</div>
						<div class="form-group">
							<label>Country</label>
							<form:input name="country" cssClass="form-control"
								path="address[1].country" />
						</div>
						<div class="form-group">
							<label>Postal Code</label>
							<form:input name="postalCode" cssClass="form-control"
								path="address[1].postalCode" />
						</div>
						<div class="form-group">
							<label>Phone Number</label>
							<form:input name="phoneNumber" cssClass="form-control"
								path="address[1].phoneNumber" />
						</div>
						<div class="form-group">
							<label>Mobile Number(Primary)</label>
							<form:input name="mobileNumberPrimary" cssClass="form-control"
								path="address[1].mobileNumberPrimary" />
						</div>
						<div class="form-group">
							<label>Mobile Number(Secondary)</label>
							<form:input name="mobileNumberSecondary" cssClass="form-control"
								path="address[1].mobileNumberSecondary" />
						</div>
					</fieldset>
					<c:if test = "${not empty beats}">
						<fieldset>
							<legend>Beat Assignment</legend>
							<div class="form-group">
									<label>Beat</label>
									<form:select path="beatIDs" cssClass="form-control" id="beats" multiple="true">
										<c:forEach var="cbeat" items="${beats}">
											<form:option value="${ cbeat.beatID }" label="${ cbeat.name }"/>
										</c:forEach>
									</form:select>
								</div>
						</fieldset>
					</c:if>
					<div class="form_submit">
						<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
						<button type="button" class="btn btn-primary" id="resetBtn" onclick="location.reload();">Reset</button>
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
	$(document).ready(function() {
		$("#main_cp").prop('required',true);
	});
	$(document).ready(function() {
		$("#main_add1").prop('required',true);
	});
	$(document).ready(function() {
		$("#main_city").prop('required',true);
	});
	$(document).ready(function() {
		$("#main_state").prop('required',true);
	});
	$(document).ready(function() {
		$("#main_cntry").prop('required',true);
	});
	$(document).ready(function() {
		$("#main_postCode").prop('required',true);
	});
	$(document).ready(function() {
		$("#main_mobPri").prop('required',true);
	});
	
</script>

</html>
