<!DOCTYPE html>
<%@page import="com.sales.crm.model.DeliveryBookingSchedule"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="com.fasterxml.jackson.databind.ObjectWriter"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.sales.crm.model.Beat"%>
<%@ page import="com.sales.crm.model.TrimmedCustomer"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">

<head>
<title>Delivery Booking</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
<link href="<%=request.getContextPath()%>/resources/css/bootstrap-datepicker.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/resources/js/bootstrap-datepicker.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/jquery.dataTables.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/dataTables.bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/css/dataTables.bootstrap.min.css"></script>


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

fieldset {
	border: 1px solid grey;
	padding: 10px;
	border-radius: 5px;
}

legend {
	width: auto !important;
	border-bottom: 0px !important;
}

table.table.table-striped thead {
	background: #ddd;
	padding: 10px 0 10px 0;
}

.table {
	width: 100%;
	max-width: 100%;
	margin-bottom: 20px;
	margin-top: 10px;
}

.modal-custom-footer {
	padding: 15px;
	text-align: center;
	border-top: 1px solid #e5e5e5;
}

.dataTables_paginate {
    margin-top: -20px;
    text-align: right;
    float: right;
    display: block
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
				<h3>Search Delivery Bookings</h3>
			</div>
			<div class="col-md-8 add_customer">
				<% if(resourcePermIDs.contains(ResourcePermissionEnum.ORDER_SCHEDULE_DELIVERY_BOOKING.getResourcePermissionID())) { %>
				<button type="submit" class="btn btn-primary"
					onclick="location.href='<%=request.getContextPath()%>/web/deliveryExecWeb/scheduleDeliveryBookingForm';">New
					Delivery Booking</button>
				<% } %>
			</div>
		</div>
		<div class="row top-height">
			<div class="col-md-8 ">
				<form:form modelAttribute="deliveryBookingSchedule" class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3">Visit Date</label>
						<div class="col-sm-4">
							<form:input id="dp" name="visitDate" cssClass="dp form-control"
								path="visitDate" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3">Delivery Executive</label>
						<div class="col-sm-4">
							<form:select path="delivExecutiveID" cssClass="form-control"
								id="deliv_exec">
								<option value="-1" label="--- Select Delivery Exec---" />
								<form:options items="${delivExecs}" itemValue="userID"
									itemLabel="name" />
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3">Beats</label>
						<div class="col-sm-4">
							<form:select path="beatID" cssClass="form-control" id="beats">
								<option value="-1" label="--- Select Beat---" />
								<form:options items="${beats}" itemValue="beatID"
									itemLabel="name" />
							</form:select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3">Customer</label>
						<div class="col-sm-4">
							<form:select path="customerID" cssClass="form-control"
								id="customers">
								<form:option value="-1" label="--- Select ---" />
								<form:options items="${customers}" itemValue="customerID"
									itemLabel="customerName" />
							</form:select>
						</div>	
					</div>
					<div>
						<button type="button" class="btn btn-primary" id="search" disabled>Search</button>
					</div>
					<form:hidden name="tenantID" path="tenantID" id="tenantID"
						value="${ tenantID }" />
				</form:form>	
			</div>	
			<div>
						<table class="table table-striped" id="myTable">
							<thead>
								<tr>
									<th>Order ID</th>
									<th>Visit Date</th>
									<th>Customer Name</th>
									<th>Beat</th>
									<th>Delivery Executive</th>
									<th>Action</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${fn:length(deliveriesBookedForToday) gt 0}">
									<c:forEach var="deliveryBookingSchedule"
										items="${deliveriesBookedForToday}">
										<%
										ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
										String json = ow.writeValueAsString(((DeliveryBookingSchedule)pageContext.getAttribute("deliveryBookingSchedule")).getOrders());
										%>
										<tr>
											<td><a href=# id=order-link data-toggle=modal data-target=#orders data-rows='<%=json%>'>${deliveryBookingSchedule.orderIDs[0]}</a></td>
											<td>${deliveryBookingSchedule.visitDateAsString}</td>
											<td>${deliveryBookingSchedule.customerName}</td>
											<td>${deliveryBookingSchedule.beatName}</td>
											<td>${deliveryBookingSchedule.delivExecutiveName}</td>
											<td><a href="#" id="link"
												data-params=${deliveryBookingSchedule.ID
												} data-params1=${deliveryBookingSchedule.customerID}>Cancel</a>
											</td>
										</tr>
									</c:forEach>
								</c:if>
								<c:if test="${fn:length(deliveriesBookedForToday) eq 0}">
									<tr>
										<td>No order booking is scheduled.</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								</c:if>
							</tbody>
						</table>
						</div>	
		</div>
	</div>
	<script type="text/javascript">
		//Sales Execs
		$(document).ready(function() {
			$('#myTable').DataTable({searching: false, aaSorting: [], bLengthChange: false, pageLength: 10});
			
			$('#dp').datepicker({format: 'dd-mm-yyyy'});
			
			$('#dp').blur(function() {
				if( $('#dp').val() ) {
					$('#search').prop('disabled', false);
				}else if ( $('#deliv_exec').val() != -1 || $('#beats').val() != -1 || $('#customers').val() != -1){
					$('#search').prop('disabled', false);
				}else{
					$('#search').prop('disabled', true);
				}
			})
			
			$('#deliv_exec').change(function() {
				if( $('#deliv_exec').val() != -1 ) {
					$('#search').prop('disabled', false);
				}else if ( $('#dp').val() != "" || $('#beats').val() != -1 || $('#customers').val() != -1 ){
					$('#search').prop('disabled', false);
				}else{
					$('#search').prop('disabled', true);
				}
			})
			
			$('#beats').change(function() {
				if( $('#beats').val() != -1 ) {
					$('#search').prop('disabled', false);
				}else if ( $('#deliv_exec').val() != -1 || $('#dp').val() != "" || $('#customers').val() != -1 ){
					$('#search').prop('disabled', false);
				}else{
					$('#search').prop('disabled', true);
				}
			})
			
			$('#customers').change(function() {
				if( $('#customers').val() != -1 ) {
					$('#search').prop('disabled', false);
				}else if ( $('#deliv_exec').val() != -1 || $('#beats').val() != -1 || $('#dp').val() != "" ){
					$('#search').prop('disabled', false);
				}else{
					$('#search').prop('disabled', true);
				}
			})
			
			$(document).on('click', "#link", function (e){
				var dataFound = 0;
				//Hack
				var date = "";
				if($('#dp').val()){
					date=$('#dp').val();
				}

				var delivExecID =  $('#deliv_exec').val();
				var beatID = $('#beats').val();
				var customerID = $('#customers').val();	
				var tenantID = $('#tenantID').val();
				//This is when non of the search options are selected and on landing page
				if(delivExecID == -1 && beatID == -1 && customerID == -1 && date == ""){
					var today = new Date();
					var dd = today.getDate();
					var mm = today.getMonth()+1; 
					var yyyy = today.getFullYear();
					if(dd<10) 
					{
					    dd='0'+dd;
					} 

					if(mm<10) 
					{
					    mm='0'+mm;
					} 
					//06-05-2021
					date = dd+'-'+mm+'-'+yyyy;
				}
				 $.ajax({
						type : "GET",
						url : "/crm/rest/orderReST/unscheduleOrderDelivery/"+$(this).data('params')+"/"+$('#tenantID').val(),
						dataType : "json",
						success : function(data) {
							$("#successModal").modal('show');
							$.ajax({
								type: 'POST',
								contentType : 'application/json',
							    url: '/crm/rest/orderReST/scheduledOrderDeliveries/search',
							    data: '{"date":"'+ date + '", "delivExecID":' + delivExecID + ', "beatID":' + beatID + ' , "customerID":' + customerID + ', "tenantID":' + tenantID + '}', 
							    success : function(data) {
									$("#myTable > tbody").empty();
									var result = data.businessEntities;
									$.each(result,function(i,obj) {
										dataFound = 1;
										var row_data = "<tr><td><a href=# id=order-link data-toggle=modal data-target=#orders data-rows='" + JSON.stringify(obj.orders) + "'>"+obj.orderIDs[0]+"</a></td><td>"+obj.visitDateAsString+"</td><td>"+obj.customerName+"</td><td>"+obj.beatName+"</td><td>"+obj.delivExecutiveName+"</td><td><a href=# id=link data-params="+obj.id+">Cancel</a></td></tr>";
										$("#myTable > tbody").append(row_data);
									});
									if(dataFound == 0){
										var row_data = "<tr><td>No delivery booking is scheduled.</td><td></td><td></td><td></td><td></td><td></td></tr>";
										 $("#myTable > tbody").append(row_data);
									}
								},
								error: function(jq,status,message) {
									$("#listModal").modal('show');
						        }
							});
						},
						error: function(jq,status,message) {
							$("#errorModal").modal('show');
				        }
				});
			}); 
			
			
			//Search visits
			$('#search').click(function(){
				var dataFound = 0;
				//Hack
				var date = "";
				if($('#dp').val()){
					date=$('#dp').val();
				}
				
				var delivExecID =  $('#deliv_exec').val();
				var beatID = $('#beats').val();
				var customerID = $('#customers').val();	
				var tenantID = $('#tenantID').val();
				$.ajax({
						type: 'POST',
						contentType : 'application/json',
						url: '/crm/rest/orderReST/scheduledOrderDeliveries/search',
					    data: '{"date":"'+ date + '", "delivExecID":' + delivExecID + ', "beatID":' + beatID + ' , "customerID":' + customerID + ', "tenantID":' + tenantID + '}', 
					    success : function(data) {
							$("#myTable > tbody").empty();
							var result = data.businessEntities;
							$.each(result,function(i,obj) {
								dataFound = 1;
								var row_data = "<tr><td><a href=# id=order-link data-toggle=modal data-target=#orders data-rows='" + JSON.stringify(obj.orders) + "'>"+obj.orderIDs[0]+"</a></td><td>"+obj.visitDateAsString+"</td><td>"+obj.customerName+"</td><td>"+obj.beatName+"</td><td>"+obj.delivExecutiveName+"</td><td><a href=# id=link data-params="+obj.id+">Cancel</a></td></tr>";
								$("#myTable > tbody").append(row_data);
							});
							if(dataFound == 0){
								var row_data = "<br>No order booking is scheduled.";
								 $("#myTable > tbody").append(row_data);
							}
						},
						error: function(jq,status,message) {
							$("#listModal").modal('show');
				        }
					});
			});
		});
			
		</script>


	<div class="modal fade" id="successModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<b>Success</b>
				</div>
				<div class="modal-body">Scheduled order delivery has been
					successfully canceled.</div>
				<div class="modal-custom-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="errorModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<b>Error !!</b>
				</div>
				<div class="modal-body">Scheduled order delivery can't be
					successfully canceled. Please try again after sometime, if error
					persists contact System Administrator.</div>
				<div class="modal-custom-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="listModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<b>Error !!</b>
				</div>
				<div class="modal-body">Scheduled order delivery list could not
					be fetched successfully. Please try again after sometime, if error
					persists contact System Administrator.</div>
				<div class="modal-custom-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Order List Modal -->
	<div class="modal fade" id="orders" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<b>Order included for the delivery</b>
				</div>
				<div class="modal-body">
					<table class="table" id="modalTable">
						<thead>
							<tr>
								<th>Order ID</th>
								<th>Order Booking ID</th>
								<th>Order Date</th>
								<th>No Of Line Items</th>
								<th>Booking Value</th>
								<th>Remark</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="modal-custom-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
				</div>
				<script type="text/javascript">
					var customerID;
					$('#orders').on('show.bs.modal', function (e) {
						$("#modalTable > tbody").empty();
						var datarows = $(e.relatedTarget).data('rows');
						$.each( datarows, function( index, value ) {
							var row_data = "<tr><td>"+ value.orderID +"</td><td>"+ value.orderBookingID +"</td><td>"+ value.dateCreatedString +"</td><td>"+ value.noOfLineItems +"</td><td>"+ value.bookValue +"</td><td>"+ value.remark +"</td></tr>";
							$("#modalTable > tbody").append(row_data);
						});
					});
				</script>

			</div>
		</div>
	</div>
</body>
</html>
