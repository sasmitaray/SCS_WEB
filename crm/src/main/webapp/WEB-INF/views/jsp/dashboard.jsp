<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">

<head>
<title>Customers</title>
<!-- Bootstrap Core CSS -->
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
<script
	src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
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

.upload_input {
	display: block;
	visibility: hidden;
	width: 0;
	height: 0;
}

table.table.table-striped thead {
	background: #ddd;
	padding: 10px 0 10px 0;
}

input[type=file] {
	display: inline-block;
}

.table {
	width: 100%;
	max-width: 100%;
	margin-bottom: 20px;
	margin-top: 10px;
	height: 280px;
}

.box-style {
	height: 150px;
	width: 223px;
	background-color: #f5f5f5;
	box-shadow: 10px 10px 5px #888888;
	border-style: outset;
	margin-right: 10px
}

.table-box-style {
	height: 300px;
	background-color: #f5f5f5;
	box-shadow: 10px 10px 5px #888888;
	border-style: outset;
	width: 99%;
}

.numberCircle {
	border-radius: 50%;
	width: 80px;
	font-size: 32px;
	border: 2px solid #666;
	margin-left: 60px;
}

.numberCircle span {
	text-align: center;
	line-height: 80px;
	display: block;
}

#schSummTbl tr {
	width: 100%;
	display: inline-table;
	table-layout: fixed;
}

#schSummTbl table {
	height: 300px;
	display: -moz-groupbox;
}

#schSummTbl tbody {
	overflow-y: scroll;
	height: 150px;
	width: 98%;
	position: absolute;
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
				<h3>Dashboard</h3>
			</div>
		</div>
		<div class="row">
			<div class="col-md-1 box-style">
				<p align="center"><b>Customers</b></p>
				<div class="numberCircle"><span><a href="<%=request.getContextPath()%>/web/customerWeb/list">${ numberOfCustomers }</a></span></div>
			</div>
			<div class="col-md-1 box-style">
				<p align="center"><b>Manufacturers</b></p>
				<div class="numberCircle"><span><a href="<%=request.getContextPath()%>/web/manufacturerWeb/list">${ numberOfManufacturers }</a></span></div>
			</div>
			<div class="col-md-1 box-style">
				<p align="center"><b>Beats</b></p>
				<div class="numberCircle"><span><a href="<%=request.getContextPath()%>/web/beatWeb/list">${ numberOfBeats }</a></span></div>
			</div>
			<div class="col-md-1 box-style">
				<p align="center"><b>Sales Executives</b></p>
				<div class="numberCircle"><span><a href="<%=request.getContextPath()%>/web/userWeb/list/2">${ numberOfSalesExecs }</a></span></div>
			</div>
			<div class="col-md-1 box-style">
				<p align="center"><b>Delivery Executives</b></p>
				<div class="numberCircle"><span><a href="<%=request.getContextPath()%>/web/userWeb/list/3">${ numberOfDeliveryExecs }</a></span></div>
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-md-12 table-box-style">
				<div class="row">
					<div class="col-md-2" style="padding-right: 1px;">
							<label>Order Schedule Summary</label>
					</div>
				</div>
				<div class="row">
					<form id="searchForm">
						<div class="col-md-2" style="padding-right: 1px;">
							<div class="form-group">
								<select class="form-control"
									id="sales_exec">
									<option value="-1" label="-- Sales Executive --" />
									<c:forEach var="salesExec" items="${salesExecs}">
										<option value="${ salesExec.userID }"
											label="${ salesExec.name }"
											/>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-2" style="padding-right: 1px;">
							<input id="dp" name="visitDate" class="dp form-control" type="text" value="--Visit Date--">
						</div>
						<div class="col-md-2">
							<button type="submit" id="search" class="btn btn-primary"
								disabled>Search</button>
						</div>
					</form>
				</div>
				<table class="table table-striped" id="schSummTbl">
					<thead>
						<tr>
							<th>Schedule ID</th>
							<th>Schedule Date</th>
							<th>Sales Executive</th>
							<th>Orders Scheduled</th>
							<th>Orders Completed</th>
							<th>Orders Pending</th>
							<th>No of Line items</th>
							<th>Book Value</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(scheduledOrderSummaries) gt 0}">
							<c:forEach var="scheduledOrderSummarie" items="${scheduledOrderSummaries}">  
								<tr>
									<td><a href="<%=request.getContextPath()%>/web/orderWeb/orderScheduleReport/${ scheduledOrderSummarie.scheduleID }">${ scheduledOrderSummarie.scheduleID }</a></td>
									<td>${ scheduledOrderSummarie.visitDateStr }</td>
									<td>${ scheduledOrderSummarie.salesExecName }</td>
									<td>${ scheduledOrderSummarie.numberOfSchedules }</td>
									<td>${ scheduledOrderSummarie.numberOfOrders }</td>
									<td>${ scheduledOrderSummarie.numberOfOrdersPending }</td>
									<td>${ scheduledOrderSummarie.numberOfLines }</td>
									<td>${ scheduledOrderSummarie.totalBookValue }</td>
								</tr>
							</c:forEach>
						</c:if>
						<c:if test="${fn:length(scheduledOrderSummaries) eq 0}">
							<tr><td>No schedules available.</td><td></td><td></td><td></td></tr>
						</c:if>	
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		
		$('#sales_exec').change(function(){
			if($("#sales_exec").prop('selectedIndex') != 0){
				$('#search').prop('disabled', false);
			}else{
				$('#search').prop('disabled', true);
			}
		});
		
		$('#dp').blur(function() {
			if($("#dp").val() != "--Visit Date--" && $('#dp').val()){
				$('#search').prop('disabled', false);
			}else{
				$('#search').prop('disabled', true);
			}
		});
		
		$('#dp').datepicker({format: 'dd-mm-yyyy'});
		
		$('#search').click(function(){
			var dataFound = 0;
			//Hack
			var date = "-";
			if($("#dp").val() != "--Visit Date--" && $('#dp').val()){
				date=$('#dp').val();
			}
			$.ajax({
				type : "GET",
				url : "${contextPath}/rest/orderReST/dashboardOrderScheduleSummary/"+ date +"/"+ $('#sales_exec').val(),
				dataType : "json",
				success : function(data) {
					$("#schSummTbl > tbody").empty();
					var result = data.businessEntities;
					$.each(result,function(i,schedule) {
						dataFound = 1;
						row_data = "<tr><td><a href=${contextPath}/web/orderWeb/orderScheduleReport/"+schedule.scheduleID+">"+schedule.scheduleID+"</a></td><td>"+schedule.visitDateStr+"</td><td>"+schedule.salesExecName+"</td><td>"+schedule.numberOfSchedules+"</td><td>"+schedule.numberOfOrders+"</td><td>"+schedule.numberOfOrdersPending+"</td><td>"+schedule.numberOfLines+"</td><td>"+schedule.totalBookValue+"</td></tr>";
		                $("#schSummTbl > tbody").append(row_data);
					});
					if(dataFound == 0){
						var row_data = "<br>No Schedules available.";
						 $("#schSummTbl > tbody").append(row_data);
					}
				}
			});
			return false;
		});
	});
</script>
</html>
