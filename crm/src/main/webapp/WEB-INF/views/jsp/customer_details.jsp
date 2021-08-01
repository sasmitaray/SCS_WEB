<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.sales.crm.util.EncodeDecodeUtil"%>
<%@page import="com.sales.crm.model.Customer"%>
<html lang="en">

<head>
	<title>Customer Details</title>
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
	margin-bottom: 10px;
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

.form-group {
    margin-bottom: 0;
}

.form-group.required .control-label:after { 
   content:"*";
   color:red;
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
		<%@ include file="menus.jsp" %>
		<div class="row customer_list">
			<div class="col-md-4">
				<h2>Customers Details</h2>
			</div>
		</div>
		<div class="col-md-9 add_customer">
				<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
				<c:if test = "${customer.statusID == 2}">
					<% if(resourcePermIDs.contains(ResourcePermissionEnum.CUSTOMER_UPDATE.getResourcePermissionID())) { %>
					<button type="submit" class="btn btn-primary"
						onclick="location.href='<%=request.getContextPath()%>/web/customerWeb/editCustomerForm/${customer.code}';">
						Modify</button>
					<% } %>
				
				
					<button type="submit" class="btn btn-primary" id="deactivateBtn" data-toggle="modal" data-target="#deactivateModal">
								Deactivate</button>
					
					<% if(resourcePermIDs.contains(ResourcePermissionEnum.CUSTOMER_DELETE.getResourcePermissionID())) { %>
						<c:choose>
							<c:when test = "${customer.hasTransactions}">
								<a href=# id=link class="btn btn-primary" data-toggle=modal data-target=#confirm-submit>Delete</a>	
							</c:when>
							<c:otherwise>
									<button type="submit" class="btn btn-primary" id="deleteBtn" data-toggle="modal" data-target="#confirm">
										Delete</button>
							</c:otherwise>				
						</c:choose>	
					<% } %>
				</c:if>
				
				<c:if test = "${customer.statusID == 3}">
					<button type="submit" class="btn btn-primary" id="activateBtn" data-toggle="modal" data-target="#activateModal">
							Activate Customer</button>
				</c:if>
		</div>
		<div class="row top-height">
			<div class="col-md-9 ">

				<fieldset>
					<legend>Customer Details</legend>
					<div>
						<label>Name : </label> <span>${customer.name}</span>
					</div>
					<div class="form-group">
						<label>Description :</label> <span>${customer.description}</span>
					</div>
					<input type="hidden" name="tenantID" id="tenantID" value="${ customer.tenantID }" />
				</fieldset>

				<fieldset>
					<legend>Main Address</legend>
					<div class="form-group">
						<label>Contact Person :</label> <span>${customer.address[0].contactPerson}</span>
					</div>
					<div class="form-group">
						<label>Address Line 1 :</label> <span>${customer.address[0].addressLine1}</span>
					</div>
					<div class="form-group">
						<label>Address Line 2 :</label> <span>${customer.address[0].addressLine2}</span>
					</div>
					<div class="form-group">
						<label>Street :</label> <span>${customer.address[0].street}</span>
					</div>
					<div class="form-group">
						<label>City :</label> <span>${customer.address[0].city}</span>
					</div>
					<div class="form-group">
						<label>State :</label> <span>${customer.address[0].state}</span>
					</div>
					<div class="form-group">
						<label>Country : </label> <span>${customer.address[0].country}</span>
					</div>
					<div class="form-group">
						<label>Postal Code :</label> <span>${customer.address[0].postalCode}</span>
					</div>
					<div class="form-group">
						<label>Phone Number :</label> <span>${customer.address[0].phoneNumber}</span>
					</div>
					<div class="form-group">
						<label>Mobile Number(Primary) :</label> <span>${customer.address[0].mobileNumberPrimary}</span>
					</div>
					<div class="form-group">
						<label>Mobile Number(Secondary) :</label> <span>${customer.address[0].mobileNumberSecondary}</span>
					</div>
				</fieldset>
				<c:if test="${not empty customer.address[1]}">
					<fieldset>
						<legend>Billing Address</legend>
						<div class="form-group">
							<label>Contact Person :</label> <span>${customer.address[1].contactPerson}</span>
						</div>
						<div class="form-group">
							<label>Address Line 1 :</label> <span>${customer.address[1].addressLine1}</span>
						</div>
						<div class="form-group">
							<label>Address Line 2 :</label> <span>${customer.address[1].addressLine2}</span>
						</div>
						<div class="form-group">
							<label>Street :</label> <span>${customer.address[1].street}</span>
						</div>
						<div class="form-group">
							<label>City :</label> <span>${customer.address[1].city}</span>
						</div>
						<div class="form-group">
							<label>State :</label> <span>${customer.address[1].state}</span>
						</div>
						<div class="form-group">
							<label>Country : </label> <span>${customer.address[1].country}</span>
						</div>
						<div class="form-group">
							<label>Postal Code :</label> <span>${customer.address[1].postalCode}</span>
						</div>
						<div class="form-group">
							<label>Phone Number :</label> <span>${customer.address[1].phoneNumber}</span>
						</div>
						<div class="form-group">
							<label>Mobile Number(Primary) :</label> <span>${customer.address[1].mobileNumberPrimary}</span>
						</div>
						<div class="form-group">
							<label>Mobile Number(Secondary) :</label> <span>${customer.address[1].mobileNumberSecondary}</span>
						</div>
					</fieldset>
				</c:if>
				<c:if test="${not empty customer.deactivationReason}">
					<fieldset>
						<legend>Deactivation Details</legend>
						<div class="form-group">
							<label>Deactivation Date :</label> <span>${customer.deactivationDateStr}</span>
						</div>
						<div class="form-group">
							<label>Deactivation Reason :</label> <span>${customer.deactivationReason}</span>
						</div>
					</fieldset>
				</c:if>
			</div>
		</div>
		<div class="col-md-9 add_customer_buttom">
				<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
				<c:if test = "${customer.statusID == 2}">
					<% if(resourcePermIDs.contains(ResourcePermissionEnum.CUSTOMER_UPDATE.getResourcePermissionID())) { %>
					<button type="submit" class="btn btn-primary"
						onclick="location.href='<%=request.getContextPath()%>/web/customerWeb/editCustomerForm/${customer.code}';">
						Modify</button>
					<% } %>
				
					<button type="submit" class="btn btn-primary" id="deactivateBtn" data-toggle="modal" data-target="#deactivateModal">
								Deactivate</button>
					
					<% if(resourcePermIDs.contains(ResourcePermissionEnum.CUSTOMER_DELETE.getResourcePermissionID())) { %>
						<c:choose>
							<c:when test = "${customer.hasTransactions}">
								<a href=# id=link class="btn btn-primary" data-toggle=modal data-target=#confirm-submit>Delete</a>	
							</c:when>
							<c:otherwise>
									<button type="submit" class="btn btn-primary" id="deleteBtn" data-toggle="modal" data-target="#confirm">
										Delete</button>
							</c:otherwise>				
						</c:choose>	
					<% } %>
				</c:if>
				
				<c:if test = "${customer.statusID == 3}">
					<button type="submit" class="btn btn-primary" id="activateBtn" data-toggle="modal" data-target="#activateModal">
							Activate</button>
				</c:if>
				
				
			</div>
	</div>
	<div class="modal fade" id="confirm" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<b>Confirm removal of customer.</b>
				</div>
				<div class="modal-body">
					Are you sure you want to remove the customer, <span><b>${customer.name} ?</b></span> 
				</div>
				<div class="modal-custom-footer">
					<button type="submit" id="delete" class="btn btn-primary">Confirm</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					<script type="text/javascript">
						//$('#delete').click(function(){
						//   window.location.href = "${contextPath}/web/customerWeb/delete/${customer.customerID}"
						//});
						//Click on confirm button
						$('#delete').click(function(e){
						   $.ajax({
							    type: 'DELETE',
							    url: '${contextPath}/rest/customer/delete',
							    data: '{"customerID":${customer.customerID},"tenantID":${customer.tenantID}}', 
							    success: function(data) { 
							    	window.location.href = "${contextPath}/web/customerWeb/delete/result/0"
							    },
							    error: function() {
							    	window.location.href = "${contextPath}/web/customerWeb/delete/result/1"
							     },
							    contentType: "application/json",
							    dataType: 'json'
							});
						});
					</script>
				</div>
			</div>
		</div>
	</div>
	
	
	<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<b>Warning !</b>
			</div>
				<div class="modal-body">
					This customer has active transactions in the system, so the customer can't be removed. 
					The customer's information will be used for any future analysis and removing customer from the system will remove all the information
					related to that customer. You can deactivate this customer instead of removing from the system.
				</div>
				<div class="modal-custom-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
			</div>
		</div>
	</div>
	</div>


	<div class="modal fade" id="deactivateModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<b>Confirm de-activation of customer.</b>
				</div>
				<div class="modal-body">
					<div class="form-group required">
						<label class='control-label'>Deactivation Reason</label>
					</div>

					<div class="md-form">
						<textarea maxlength="1000" type="text" id="reason" class="md-textarea form-control"
							rows="4"></textarea>
					</div>
					<label id="deactMSG" style="color:red; font-style: italic; font-weight: normal;">Please mention deactivation reason above.</label>
							
				</div>
				<div class="modal-custom-footer">
					<button type="submit" id="deactivate" class="btn btn-primary">Confirm</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					<script type="text/javascript">
						$(document).ready(function(e) {
							//If error message is already there will disapear once some text is entered
							$('#reason').bind('input propertychange', function() {
							     if(this.value.length){
							    	  $("#deactMSG").hide();
							      }else{
							    	  $("#deactMSG").show();
							      }
							});
							
							//When the modal is shown, clean up all the data 
							$('#deactivateModal').on('show.bs.modal', function (e) {
								$("#deactMSG").hide();
								$('#reason').val('');
							});
							
							//Click on confirm button
							$('#deactivate').click(function(e){
								var reason = $('#reason').val();
							  	//If deactivation reason is not mentioned, show the error message.
								if($('#reason').val().trim() == ""){
						    	 	$("#deactMSG").show();
						    	}else{
								   $.ajax({
									    type: 'POST',
									    url: '${contextPath}/rest/customer/deactivate',
									    data: '{"customerID":${customer.customerID},"tenantID":${customer.tenantID}, "remark":"'+reason+'"}', 
									    success: function(data) { 
									    	window.location.href = "${contextPath}/web/customerWeb/deactivate/result/${customer.code}/0"
									    },
									    error: function() {
									    	window.location.href = "${contextPath}/web/customerWeb/deactivate/result/${customer.code}/1"
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
	
	<div class="modal fade" id="activateModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<b>Confirm activation of customer.</b>
				</div>
				<div class="modal-body">
					<div class="form-group required">
						<label class='control-label'>Activation Reason</label>
					</div>

					<div class="md-form">
						<textarea maxlength="1000" type="text" id="actreason" class="md-textarea form-control"
							rows="4"></textarea>
					</div>
					<label id="actMSG" style="color:red; font-style: italic; font-weight: normal;">Please mention activation reason above.</label>
				</div>
				<div class="modal-custom-footer">
					<button type="submit" id="activate" class="btn btn-primary">Confirm</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					<script type="text/javascript">
						$(document).ready(function(e) {
							//If error message is already there will disapear once some text is entered
							$('#actreason').bind('input propertychange', function() {
							     if(this.value.length){
							    	  $("#actMSG").hide();
							      }else{
							    	  $("#actMSG").show();
							      }
							});
							
							//When the modal is shown, clean up all the data 
							$('#activateModal').on('show.bs.modal', function (e) {
								$("#actMSG").hide();
								$('#actreason').val('');
							});
							
							//Click on confirm button
							$('#activate').click(function(e){
								var actreason = $('#actreason').val();
							  	//If activation reason is not mentioned, show the error message.
								if($('#actreason').val().trim() == ""){
						    	 	$("#actMSG").show();
						    	}else{
								   $.ajax({
									    type: 'POST',
									    url: '${contextPath}/rest/customer/activate',
									    data: '{"customerID":${customer.customerID},"tenantID":${customer.tenantID}, "remark":"'+actreason+'"}', 
									    success: function(data) { 
									    	window.location.href = "${contextPath}/web/customerWeb/activate/result/${customer.code}/0"
									    },
									    error: function() {
									    	window.location.href = "${contextPath}/web/customerWeb/activate/result/${customer.code}/1"
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
</body>
</html>
