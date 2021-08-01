<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">

<head>
	<title>Edit Area</title>
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
				<h2>Edit Order</h2>
				<form:form modelAttribute="order" method="post"
					action="${contextPath}/web/orderWeb/update">
					<fieldset>
						<legend>Order Details</legend>
						<div class="form-group">
							<label>Order ID</label>
							<form:input name="orderID" cssClass="form-control"
								path="orderID" value="${ order.orderID }" readonly="true"/>
						</div>
						<div class="form-group">
							<label>Order Schedule ID</label>
							<form:input name="orderBookingID" cssClass="form-control"
								path="orderBookingID" value="${ order.orderBookingID }" readonly="true"/>
						</div>
						<div class="form-group">
							<label>Customer Name</label>
							<form:input name="customerName" cssClass="form-control"
								path="customerName" value="${ order.customerName }" readonly="true"/>
						</div>
						<div class="form-group required">
							<label class='control-label'>Number Of Line Items</label>
							<form:input name="noOfLineItems" cssClass="form-control"
								path="noOfLineItems" id="noOfLineItems" value="${ order.noOfLineItems }"/>
						</div>
						<div class="form-group required">
							<label class='control-label'>Aproximate Order Value</label>
							<form:input name="bookValue" cssClass="form-control"
								path="bookValue" id="bookValue" value="${ order.bookValue }" />
						</div>
						<div class="form-group required">
							<label class='control-label'>Remarks</label>
							<form:input name="remark" cssClass="form-control"
								path="remark" id="remark" value="${ order.remark }"/>
						</div>
						<div class="form-group">
							<label>Order Status</label>
							<form:input name="statusAsString" cssClass="form-control"
								path="statusAsString" value="${ order.statusAsString }" readonly="true"/>
						</div>
						<div class="form-group">
							<label>Order Creation Date</label>
							<form:input name="dateCreatedString" cssClass="form-control"
								path="dateCreatedString" value="${ order.dateCreatedString }" readonly="true"/>
						</div>
					</fieldset>
					<div>
						<form:hidden path="tenantID" name="tenantID" value="${ order.tenantID }"/>
						<form:hidden path="customerID" name="customerID" value="${ order.customerID }"/>
						<form:hidden path="statusID" name="status" value="${ order.statusID }"/>
						<form:hidden path="code" name="code" value="${ order.code }"/>
					</div>
					<div class="form_submit">
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
	   		$("#noOfLineItems").prop('required',true);
	   		$("#bookValue").prop('required',true);
	   		$("#remark").prop('required',true);
		});
	</script>
</body>

</html>
