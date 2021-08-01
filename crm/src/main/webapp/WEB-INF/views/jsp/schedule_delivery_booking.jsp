<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.sales.crm.model.Beat"%>
<%@ page import="com.sales.crm.model.TrimmedCustomer"%>

<html lang="en">

<head>
<title>Schedules Delivery Booking</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" />
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">
<script src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
<link href="<%=request.getContextPath()%>/resources/css/bootstrap-datepicker.css" rel="stylesheet">
<script	src="<%=request.getContextPath()%>/resources/js/bootstrap-datepicker.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>


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

.form_submit {
	margin-top: 14px;
	text-align: right;
}

.form-group.required .control-label:after { 
   content:"*";
   color:red;
}

.modal-custom-footer {
    padding: 15px;
    text-align: center;
    border-top: 1px solid #e5e5e5;
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
		<%@ include file="menus.jsp" %>
		<div class="row top-height">
			<div class="col-md-6 ">
				<form:form modelAttribute="deliveryBookingSchedule" method="post"
					action="${contextPath}/web/deliveryExecWeb/scheduleDeliveryBooking" name="myForm" id="myForm">
					<fieldset>
						<legend>Schedule Delivery Booking</legend>
						<div class="form-group required">
							<label class='control-label'>Visit Date</label>
							<form:input id="dp" name="visitDate" cssClass="dp form-control"
								path="visitDate" />
						</div>
						<div class="form-group required">
							<label class='control-label'>Delivery Executive</label>
							<form:select path="delivExecutiveID" cssClass="form-control"
								id="deliv_exec">
								<form:option value="-1" label="--- Select Delivery Executive---" />
								<c:forEach var="delivExec" items="${delivExecs}">
									<form:option value="${ delivExec.userID }"
										label="${ delivExec.firstName } ${ delivExec.lastName }"
										id="id_trial" />
								</c:forEach>
							</form:select>
						</div>
							
						<div class="form-group required">
							<label class='control-label'>Manufacturer</label>
							<form:select path="manufacturerIDs" cssClass="form-control" id="manufs" multiple="false" >
								<option value="-1" label="--- Select Manuacturer---" />
							</form:select>
						</div>
							
						<div class="form-group required">
							<label class='control-label'>Beats</label>
							<form:select path="beatID" cssClass="form-control" id="beats">
								<option value="-1" label="--- Select Beat---" />
							</form:select>
						</div>
						<div class="form-group required">
							<label class='control-label'>Customers</label>
							<div style="width: 500px; min-height: 2px; max-height: 100px; overflow-y: auto;" id="checks">
							</div>
						</div>
						<form:hidden name="tenantID" path="tenantID" id="tenantID" value="${ tenantID }" />
					</fieldset>
					<div class="form_submit">
						<input type="button" name="btn" value="Schedule" id="submitBtn" data-toggle="modal" data-target="#confirm-submit" class="btn btn-primary" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
			
			$(document).ready(function() {
				$('#submitBtn').prop('disabled', true);
				$('#deliv_exec').change(function() {
					//Popuate Manufacturers
					var hasData = false;
					$.ajax({
							type : "GET",
							url : "${contextPath}/rest/deliveryExecReST/manufacturerParams/"+$('#deliv_exec').val() + "/"+$('#tenantID').val(),
							dataType : "json",
							success : function(data) {
								$('#manufs').empty();
								if (data.length > 1){
									$('#manufs').attr("multiple", true);
								}else{
									$('#manufs').attr("multiple", false);
								}
								$.each(data,function(i,obj) {
									hasData = true;
									if(data.length == 1){
										var div_data = "<option value="+obj.id+" selected>"+ obj.name+ "</option>";
									}else{
										var div_data = "<option value="+obj.id+">"+ obj.name+ "</option>";
									}
									$(div_data).appendTo('#manufs');
								})
								if(!hasData){
									var div_data = '<option value="-1" label="Delivery Executive is not mapped to any Manufacyurer" />';
									$(div_data).appendTo('#manufs');
								}
								
								if ( $('#dp').val() == "" || $('#beats').val() == -1 || $('#manufs').val() == -1 || $('#deliv_exec').val() == -1 ){
									$('#submitBtn').prop('disabled', true);
								}else{
									$('#submitBtn').prop('disabled', false);
								}
							}
					});
					
					$.ajax({
							type : "GET",
							url : "${contextPath}/rest/deliveryExecReST/"+$('#deliv_exec').val() + "/" + $('#tenantID').val(),
							dataType : "json",
							success : function(data) {
								$('#beats').empty();
								var div_data1="<option value=\"-1\" label=\"--- Select Beat--- \"/>";
								$(div_data1).appendTo('#beats');
								$.each(data,function(i,obj) {
									var div_data = "<option value="+obj.beatID+">"+ obj.name+ "</option>";
									$(div_data).appendTo('#beats');
								});
							}
						});
					});
				});
			
			
			$(document).ready(function() {
				$('#beats').change(function() {
					var isCusromerPresent = false;
					payload = {}
					payload ["beatID"] = $('#beats').val();
					payload ["date"] = $('#dp').val();
					payload ["tenantID"] = $('#tenantID').val();
					payload ["manufIDs"] = $('#manufs').val();
					console.log(JSON.stringify(payload));
					$.ajax({
							type : "POST",
							url : "${contextPath}/rest/customer/customersToScheduleDelivery",
							data : JSON.stringify(payload),
							contentType: "application/json",
							dataType : "json",
							success : function(data) {
								$('#checks').empty();
								$.each(data,function(i,obj) {
									isCusromerPresent = true;
									var div_data = "<input name=customerIDs id=customerIDs type=checkbox value="+obj.customer.customerID+" checked>"+obj.customer.customerName+"<input type=hidden name=customerIDmodal id="+obj.customer.customerID+"modal value="+ obj.customer.customerName +">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=# id=order-link data-toggle=modal data-target=#orders data-rows='" + JSON.stringify(obj.orders) + "'><i>View Linked Order</i></a><br>";
									//Create hidden field with Order IDs
									var orderIDs ="";
									$.each(obj.orders,function(i,order) {
										if(orderIDs == ""){
											orderIDs = orderIDs.concat(order.orderID);
										}else{
											orderIDs = orderIDs.concat("-");
											orderIDs = orderIDs.concat(order.orderID);
										}
									});
									div_data = div_data.concat("<input type=hidden name="+obj.customer.customerID+" id="+obj.customer.customerID+" value=" + orderIDs + ">");
									$(div_data).appendTo('#checks');
								});
								if(isCusromerPresent == false){
									var div_data = "<p><i>No Customers Available for order booking.</i></p>"
									$(div_data).appendTo('#checks');
									$('#submitBtn').prop('disabled', true);
								}else{
									$('#submitBtn').prop('disabled', false);
								}
							}
						});
					});
				});
			
			$('#submitBtn').click(function() {
			     /* when the button in the form, display the entered values in the modal */
			     //Sales Executive
			     var delivExId = document.getElementById("deliv_exec");
				 var delivExecName = delivExId.options[delivExId.selectedIndex].text;
			     $('#delivEX').text(delivExecName);
			     
			     //Beat
			     var beatId = document.getElementById("beats");
				 var beatName = beatId.options[beatId.selectedIndex].text;
			     $('#beat').text(beatName);
			     
			     //Customers
			     $('#customers').empty();
			     var checkedValues = $('input:checkbox[id="customerIDs"]:checked').map(function() {
			    	    return this.value;
			    	}).get();
			     $.each( checkedValues, function( key, value ) {
			    	 var listItem = "<li>"+$("#"+value+"modal").val()+"</li>";
			    	 $(listItem).appendTo('#customers');
			     });
			  	 
			  	 //Visit Date
			  	 $('#visiDate').text($("#dp").val());
			});
			
			$('#dp').datepicker({format: 'dd-mm-yyyy'});
			
			$(document).ready(function() {
	       		$("#dp").prop('required',true);
			});
			
			$(document).ready(function() {
	       		$("#deliv_exec").prop('required',true);
			});
			
			$(document).ready(function() {
	       		$("#beats").prop('required',true);
			});
			
			$(document).ready(function() {
	       		$("#checks").prop('required',true);
			});
			
		</script>

	<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"><b>Confirm Delivery Executive Visit</b></div>
				<div class="modal-body">
					Are you sure you want to schedule the visit for delivery executive ?
					<br>
					<br>
					<div>
						<label>Delivery Executive Name : </label> <span id="delivEX"></span>
					</div>
					<div>
						<label>Beat Name : </label> <span id="beat"></span>
					</div>
					<div>
						<label> Customers : </label>
						<ul id="customers"></ul>
					</div>
						<label> Visit Date : </label> <span id="visiDate"></span>
					<div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					<button type="submit" id="modalSubmit" class="btn btn-primary">Submit</button>
					
					<script type="text/javascript">
					$('#modalSubmit').click(function(){
					     /* when the submit button in the modal is clicked, submit the form */
					   $('#myForm').submit();
					});
					</script>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Order List Modal -->
	<div class="modal fade" id="orders" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header"><b>Select orders to be included in the delivery</b></div>
				<div class="modal-body">
					   <table class="table" id="myTable">
				            <thead>
				                <tr>
				                	<th></th>
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
					<button type="submit" id="ordermodalSubmit" class="btn btn-primary">Confirm</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
				</div>	
				<script type="text/javascript">
					var customerID;
					$('#ordermodalSubmit').click(function(){
						var values = "";
					    $.each($("input[id='orderLine']:checked"), function() {
					    	if(values == ""){
					    		values = $(this).val();
					    	}else{
					    		values = values.concat("-");
					    		values = values.concat($(this).val());
					    	}
					    });
					    $("#"+customerID).attr('value',values);
					   	$('#orders').modal('toggle');
					   
					});
					
					$('#orders').on('show.bs.modal', function (e) {
						$("#myTable > tbody").empty();
						var datarows = $(e.relatedTarget).data('rows');
						$.each( datarows, function( index, value ) {
							customerID = value.customerID;
							//Iterate over selected orders (set in the hidden field) and mark the checkbox checked 
							var array = $("input[name="+customerID+"]").val().split('-');
							var orderID = value.orderID;
							if($.inArray(orderID.toString(), array) > -1){
								var row_data = "<tr><td><input type=checkbox id=orderLine value="+ value.orderID +" checked></td><td>"+ value.orderID +"</td><td>"+ value.orderBookingID +"</td><td>"+ value.dateCreatedString +"</td><td>"+ value.noOfLineItems +"</td><td>"+ value.bookValue +"</td><td>"+ value.remark +"</td></tr>";
							}else{
								var row_data = "<tr><td><input type=checkbox id=orderLine value="+ value.orderID +"></td><td>"+ value.orderID +"</td><td>"+ value.orderBookingID +"</td><td>"+ value.dateCreatedString +"</td><td>"+ value.noOfLineItems +"</td><td>"+ value.bookValue +"</td><td>"+ value.remark +"</td></tr>";
							}
							$("#myTable > tbody").append(row_data);
						});
					});
				</script>
				
			</div>
		</div>
	</div>
</body>

</html>
