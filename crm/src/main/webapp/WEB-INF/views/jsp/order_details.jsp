<%@page import="com.sales.crm.model.Area"%>
<%@page import="com.sales.crm.model.Role"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.sales.crm.model.Role"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en">

<head>
<title>Order Details</title>
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
.add_customer_buttom {
	text-align: right;
	margin-top: 10px;
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
		<!-- Links -->
		<%@ include file="menus.jsp"%>
		<div class="row customer_list">
			<div class="col-md-4">
				<h2>Order Details</h2>
			</div>
		</div>	
		<div class="col-md-8 add_customer">
			<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
			<c:if test = "${order.statusID != 58}">
				<button type="submit" class="btn btn-primary" id="deactivateBtn" data-toggle="modal" data-target="#deactivateModal">
								Delete</button>
				<button type="submit" class="btn btn-primary"
					onclick="location.href='<%=request.getContextPath()%>/web/orderWeb/editOrderForm/${order.orderID}';">
						Modify</button>				
			</c:if>				
		</div>
		<div class="row top-height">
			<div class="col-md-8 ">
				<fieldset>
					<legend>Order Details</legend>
					<div class="form-group">
						<label>Order ID : </label> <span>${ order.orderID }</span>
					</div>
					<div class="form-group">
						<label>Order Schedule ID : </label> <span>${ order.orderBookingID }</span>
					</div>
					<div class="form-group">
						<label>Customer Name : </label> <span>${ order.customerName }</span>
					</div>
					<div class="form-group">
						<label>No. Of Line Items : </label> <span>${ order.noOfLineItems }</span>
					</div>
					<div class="form-group">
						<label>Aproximate Order Value : </label> <span>${ order.bookValue }</span>
					</div>
					<div class="form-group">
						<label>Remarks : </label> <span>${ order.remark }</span>
					</div>
					<div class="form-group">
						<label>Order Status : </label> <span>${ order.statusAsString }</span>
					</div>
					<div class="form-group">
						<label>Order Date : </label> <span>${ order.dateCreatedString }</span>
					</div>
				</fieldset>
			</div>
		</div>
		<div class="col-md-8 add_customer_buttom">
			<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
			<c:if test = "${order.statusID != 58}">
				<button type="submit" class="btn btn-primary" id="deactivateBtn" data-toggle="modal" data-target="#deactivateModal">
								Delete</button>
				<button type="submit" class="btn btn-primary"
					onclick="location.href='<%=request.getContextPath()%>/web/orderWeb/editOrderForm/${order.orderID}';">
						Modify</button>				
			</c:if>				
		</div>
	</div>
</body>

<div class="modal fade" id="deactivateModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<b>Confirm deletion of ordder.</b>
				</div>
				<div class="modal-body">
					<div class="form-group required">
						<label class='control-label'>Deletion Reason</label>
					</div>

					<div class="md-form">
						<textarea maxlength="1000" type="text" id="reason" class="md-textarea form-control"
							rows="4"></textarea>
					</div>
					<label id="delMSG" style="color:red; font-style: italic; font-weight: normal;">Please mention deletion reason above.</label>
							
				</div>
				<div class="modal-custom-footer">
					<button type="submit" id="deactivate" class="btn btn-primary">Confirm</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					<script type="text/javascript">
						$(document).ready(function(e) {
							//If error message is already there will disapear once some text is entered
							$('#reason').bind('input propertychange', function() {
							     if(this.value.length){
							    	  $("#delMSG").hide();
							      }else{
							    	  $("#delMSG").show();
							      }
							});
							
							//When the modal is shown, clean up all the data 
							$('#deactivateModal').on('show.bs.modal', function (e) {
								$("#delMSG").hide();
								$('#reason').val('');
							});
							
							//Click on confirm button
							$('#deactivate').click(function(e){
								var reason = $('#reason').val();
							  	//If deactivation reason is not mentioned, show the error message.
								if($('#reason').val().trim() == ""){
						    	 	$("#delMSG").show();
						    	}else{
								   $.ajax({
									    type: 'POST',
									    url: '${contextPath}/rest/orderReST/delete',
									    data: '{"orderID":${order.orderID},"tenantID":${order.tenantID}, "remark":"'+reason+'"}', 
									    success: function(data) { 
									    	window.location.href = "${contextPath}/web/orderWeb/delete/result/${order.orderID}/0"
									    },
									    error: function() {
									    	window.location.href = "${contextPath}/web/orderWeb/delete/result/${order.orderID}/1"
									     },
									    contentType: "application/json",
									    dataType: 'json'
									});
								}
							});
						});
					</script>
				</div>
			</div>
		</div>
	</div>

</html>
