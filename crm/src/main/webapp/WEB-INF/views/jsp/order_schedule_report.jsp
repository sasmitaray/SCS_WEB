<!DOCTYPE html>
<%@page import="com.sales.crm.model.OrderBookingSchedule"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">

<head>
    <title>Order Schedule Report</title>
    <!-- Bootstrap Core CSS -->
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" />
	<script src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
	<link href="<%=request.getContextPath()%>/resources/css/bootstrap-datepicker.css" rel="stylesheet">
	<script	src="<%=request.getContextPath()%>/resources/js/bootstrap-datepicker.js"></script>
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
    
    .customer_list{
    margin-bottom:20px;
    }
    .add_customer{
    text-align:right;
    margin-top:31px;
    }
    
    .side_nav_btns{
    
    margin-top:10px;
    }
    
    .side_nav_btns a{
    text-decoration: none;
    background: #337ab7;
    padding: 11px;
    border-radius: 12px;
    color: #ffffff;
    margin-top: 12px;
    
    }
    
    .the-table {
    table-layout: fixed;
    word-wrap: break-word;
	}
	
	.table {
    width: 100%;
    max-width: 100%;
    margin-bottom: 20px;
    margin-top: 10px;
	}
	
	table.table.table-striped thead {
    background: #ddd;
    padding: 10px 0 10px 0;
	}
	
	.dataTables_paginate {
    margin-top: -20px;
    position: absolute;
    text-align: right;
    left: 55%;
	}
	
	</style>
</head>

<body>
    <!-- Header -->
    <header class="dpHeaderWrap">
        <div class="text-center">
            Header part
        </div>
    </header>
    <!-- Header -->
    <div class="container">
        <%@ include file="menus.jsp" %>
       	<div class="row customer_list">
       		<div class="col-md-8">
           		<h2>Orders Schedule Report</h2>   
           	</div>
        	<div class="col-md-4 add_customer">
        	</div>
		</div>
		<div class="row top-height">
			<form:form modelAttribute="orderBookingSchedule" method="post"
				action="${contextPath}/web/orderWeb/scheduleOrderBooking" name="myForm"
				id="myForm">
				<div class="col-md-1 style="padding-right: 1px;">
					<div class="form-group">
						<label class='control-label'>Sales Executive</label>
					</div>
					<div class="form-group">
						<label class='control-label'>Beats</label>
					</div>
					<div class="form-group">
						<label class='control-label'>Visit date</label>
					</div>
					<div class="form_submit">
						<input type="button" name="btn" value="Search" id="search"
							class="btn btn-primary" />
					</div>
				</div>
				<div class="col-md-2 style="padding-left: 1px;"">
					<div class="form-group">
						<form:select path="salesExecutiveID" cssClass="form-control"
							id="sales_exec">
							<form:option value="-1" label="-- Select --" />
							<c:forEach var="salesExec" items="${salesExecs}">
								<form:option value="${ salesExec.userID }"
									label="${ salesExec.name }"
									/>
							</c:forEach>
						</form:select>
					</div>
					<div class="form-group">
						<form:select path="beatID" cssClass="form-control"
							id="beat_id">
							<form:option value="-1" label="-- Select --" />
							<c:forEach var="beat" items="${beats}">
								<form:option value="${ beat.beatID }"
									label="${ beat.name }"
									/>
							</c:forEach>
						</form:select>
					</div>
					<div class="form-group">
						<form:input id="dp" name="visitDate" cssClass="dp form-control"
							path="visitDate" />
					</div>
				</div>
				<div class="col-md-1 style="padding-right: 1px;">
					<div class="form-group">
						<label class='control-label'>Customer &nbsp;</label>
					</div>
					<div class="form-group">
						<label class='control-label'>Status</label>
					</div>
				</div>
				<div class="col-md-2">
					<div class="form-group">
						<form:select path="customerID" cssClass="form-control" id="cust_id">
							<form:option value="-1" label="-- Select --" />
							<c:forEach var="customer" items="${customers}">
								<form:option value="${ customer.customerID }" label="${ customer.customerName }" />
							</c:forEach>
						</form:select>
					</div>	
					<div class="form-group">
						<form:select path="status" cssClass="form-control" id="status">
							<form:option value="-1" label="-- Select --" />
							<option value="50">Order Scheduled</option>
							<option value="51">Order Created</option>
							<option value="52">Delivery Scheduled</option>
							<option value="53">Delivery Completed</option>
							<option value="54">Delivery Partial</option>
							<option value="55">Payment Scheduled</option>
							<option value="56">Payment Completed</option>
							<option value="57">Payment Partial</option>
						</form:select>
					</div>
				</div>
				<form:hidden name="tenantID" path="tenantID" id="tenantID" value="${ tenantID }" />
			</form:form>
		</div>
		<table class="table table-striped" id="reportTbl">
            <thead>
                <tr>
                    <th>Order Schedule ID</th>
                    <th>Order ID</th>
                    <th>Beat Name</th>
                    <th>Customer Name</th>
                    <th>Sales Exec Name</th>
                    <th>Visit Date</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            	<c:if test="${fn:length(orderBookingSchedules) gt 0}">
            		<c:forEach var="orderBookingSchedule" items="${orderBookingSchedules}">  
                		<tr>
		                 	<td>${orderBookingSchedule.bookingScheduleID}</td>
		                 	<% if(pageContext.getAttribute("orderBookingSchedule") != null &&  ((OrderBookingSchedule)pageContext.getAttribute("orderBookingSchedule")).getOrderID() != 0 ){%>
		                		<td><a href="${contextPath}/web/orderWeb/${orderBookingSchedule.orderID}">${orderBookingSchedule.orderID}</a></td>
		                	<%}else{ %>
		                		<td>-</td>
		                	<% } %>	
		                	<td>${orderBookingSchedule.beatName}</td>
		                    <td>${orderBookingSchedule.customerName}</td>
		                    <td>${orderBookingSchedule.salesExecName}</td>
		                    <td>${orderBookingSchedule.visitDateAsString}</td>
		                    <td>${orderBookingSchedule.statusAsString}</td>
		                    <% if(pageContext.getAttribute("orderBookingSchedule") != null &&  ((OrderBookingSchedule)pageContext.getAttribute("orderBookingSchedule")).getStatus() == 60 ){%>
		                		<td>Create Order</td>
		                	<%}else{ %>
		                		<td>-</td>
		                	<% } %>	
               			</tr>
                	</c:forEach>
                </c:if>
				<c:if test="${fn:length(orderBookingSchedules) eq 0}">
					<tr><td>No order booking is scheduled.</td><td></td><td></td><td></td></tr>
				</c:if>	
            </tbody>
        </table>
   	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		
		$('#dp').datepicker({format: 'dd-mm-yyyy'});
		
		$('#search').click(function(){
			var dataFound = 0;
			//Hack
			var date = "-";
			if($('#dp').val()){
				date=$('#dp').val();
			}
			$.ajax({
				type : "GET",
				url : "${contextPath}/rest/orderReST/orderScheduleReport/"+$('#sales_exec').val()+"/"+$('#beat_id').val()+"/"+date+"/"+$('#cust_id').val()+"/"+$('#status').val()+"/"+$('#tenantID').val(),
				dataType : "json",
				success : function(data) {
					$("#reportTbl > tbody").empty();
					var result = data.businessEntities;
					$.each(result,function(i,schedule) {
						dataFound = 1;
						var row_data = "<tr><td>"+schedule.bookingScheduleID+"</td>";
						var orderTD="<td>-</td>";
						if(schedule.orderID != 0){
							orderTD = "<td><a href=${contextPath}/web/orderWeb/"+schedule.orderID+">"+schedule.orderID+"</a></td>";
						}
						
						var actionTD="<td>-</td>";
						if(schedule.status == 60){
							actionTD="<td><a href=${contextPath}/web/orderWeb/createOrderForm/"+schedule.bookingScheduleID+"/"+ schedule.customerID +"/"+ schedule.customerName +">Create Order</a></td>";
						}
						row_data = row_data + orderTD +"<td>"+schedule.beatName+"</td><td>"+schedule.customerName+"</td><td>"+schedule.salesExecName+"</td><td>"+schedule.visitDateAsString+"</td><td>"+schedule.statusAsString+"</td>"+ actionTD + "</tr>";
		                $("#reportTbl > tbody").append(row_data);
					});
					if(dataFound == 0){
						var row_data = "<br>No order booking is scheduled.";
						 $("#reportTbl > tbody").append(row_data);
					}
				}
			});
			return false;
		});
		
		$('#reportTbl').DataTable({searching: false, aaSorting: [], bLengthChange: false, pageLength: 10});
	});
	
</script>
</html>
