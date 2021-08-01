<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.sales.crm.model.Beat"%>
<%@ page import="com.sales.crm.model.TrimmedCustomer"%>

<html lang="en">

<head>
<title>Schedules Order Booking</title>
<meta charset="UTF-8">
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
				<form:form modelAttribute="orderBookingSchedule" method="post"
					action="${contextPath}/web/orderWeb/scheduleOrderBooking" name="myForm" id="myForm">
					<fieldset>
						<legend>Schedule Order Booking</legend>
						<div class="form-group required">
							<label class='control-label'>Visit Date</label>
							<form:input id="dp" name="visitDate" cssClass="dp form-control"
								path="visitDate" />
						</div>
						<div class="form-group required">
							<label class='control-label'>Sales Executive</label>
							<form:select path="salesExecutiveID" cssClass="form-control"
								id="sales_exec">
								<form:option value="-1" label="--- Select Sales Executive---" />
								<c:forEach var="salesExec" items="${salesExecs}">
									<form:option value="${ salesExec.userID }"
										label="${ salesExec.firstName } ${ salesExec.lastName }"
										id="id_trial" />
								</c:forEach>
							</form:select>
							<div id="salesExecName"></div>
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
							<div style="width: 200px; min-height: 2px; max-height: 100px; overflow-y: auto;" id="custs">
							</div>
						</div>
					</fieldset>
					<form:hidden name="tenantID" path="tenantID" id="tenantID" value="${ tenantID }" />
					<div class="form_submit">
						<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
						<button type="button" class="btn btn-primary" id="resetBtn" onclick="location.reload();">Reset</button>
						<input type="button" name="btn" value="Schedule" id="submitBtn" data-toggle="modal" data-target="#confirm-submit" class="btn btn-primary" />
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
			$(document).ready(function() {
				$('#dp').datepicker({format: 'dd-mm-yyyy'});
				
				$("#sales_exec").prop('required',true);
			
				$("#beats").prop('required',true);
			
				$("#custs").prop('required',true);
				
				$('#submitBtn').prop('disabled', true);
				
				$('#dp').blur(function() {
					if( $('#dp').val() == "") {
						$('#submitBtn').prop('disabled', true);
						//Make customer list empty if pre-selected
						$('#custs').empty();
					}
				})
				
				$('#sales_exec').change(function() {
					//Popuate Manufacturers
					var hasData = false;
					$.ajax({
							type : "GET",
							url : "${contextPath}/rest/salesExecReST/manufacturerParams/"+$('#sales_exec').val() + "/"+$('#tenantID').val(),
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
									var div_data = "<option value="+obj.id+">"+ obj.name+ "</option>";
									$(div_data).appendTo('#manufs');
								})
								if(!hasData){
									var div_data = '<option value="-1" label="Sales Executive is not mapped to any Manufacyurer" />';
									$(div_data).appendTo('#manufs');
								}
								
								if ( $('#dp').val() == "" || $('#beats').val() == -1 || $('#manufs').val() == -1 || $('#sales_exec').val() == -1 ){
									$('#submitBtn').prop('disabled', true);
								}else{
									$('#submitBtn').prop('disabled', false);
								}
							}
					});
					
					
					
					
					//Get Beats	
					$.ajax({
							type : "GET",
							url : "${contextPath}/rest/salesExecReST/beats/"+$('#sales_exec').val() + "/"+$('#tenantID').val(),
							dataType : "json",
							success : function(data) {
								$('#beats').empty();
								var div_data1="<option value=\"-1\" label=\"--- Select Beat--- \"/>";
								$(div_data1).appendTo('#beats');
								$.each(data,function(i,obj) {
									var div_data = "<option value="+obj.beatID+">"+ obj.name+ "</option>";
									$(div_data).appendTo('#beats');
								});
								//Make customer list empty if pre-selected
								$('#custs').empty();
							}
					});
				});
			
			
				$('#beats').change(function() {
					var isCusromerPresent = false;
					payload = {}
					payload ["salesExecID"] = $('#sales_exec').val();
					payload ["beatID"] = $('#beats').val();
					payload ["date"] = $('#dp').val();
					payload ["tenantID"] = $('#tenantID').val();
					payload ["manufIDs"] = $('#manufs').val();
					console.log(JSON.stringify(payload));
					$.ajax({
							type : "POST",
							//url : "/crm/rest/customer/toSchedule/"+$('#sales_exec').val() + "/"+$('#beats').val() + "/"+$('#dp').val() + "/"+$('#tenantID').val(),
							url : "${contextPath}/rest/customer/toSchedule/",
							data : JSON.stringify(payload),
							contentType: "application/json",
							dataType : "json",
							success : function(data) {
								$('#custs').empty();
								$.each(data,function(i,obj) {
									isCusromerPresent = true;
									var div_data = "<input name=customerIDs id=customerIDs type=checkbox value="+obj.customerID+" checked>"+obj.customerName+"<input type=hidden id="+obj.customerID+" value="+ obj.customerName +"><br>";
									$(div_data).appendTo('#custs');
								});
								if(isCusromerPresent == false){
									var div_data = "<p><i>No Customers Available for order booking.</i></p>"
									$(div_data).appendTo('#custs');
									$('#submitBtn').prop('disabled', true);
								}else{
									$('#submitBtn').prop('disabled', false);
								}
							}
						});
					});
			
			$('#submitBtn').click(function() {
			     /* when the button in the form, display the entered values in the modal */
			     //Sales Executive
			     var salesEXId = document.getElementById("sales_exec");
				 var salesExecName = salesEXId.options[salesEXId.selectedIndex].text;
			     $('#salesEX').text(salesExecName);
			     var hidden = "<input type=hidden name=salesExecName id=salesExecName value='"+salesExecName+"'/>";
			     $(hidden).appendTo('#salesExecName');
			     
			     //Beat
			     var beatId = document.getElementById("beats");
				 var beatName = beatId.options[beatId.selectedIndex].text;
			     $('#beat').text(beatName);
			     
			     //Manufacturer
			    //  var selectedValues = $('#manufs').text();
                   
			     $('#manufacturers').empty();
                  $("#manufs :selected").each(function() {
                      var listItem = "<li>"+this.text+"</li>";
 			    	 $(listItem).appendTo('#manufacturers');
                  });
 			     
			     
			     //Customers
			     $('#customers').empty();
			     var checkedValues = $('input:checkbox:checked').map(function() {
			    	    return this.value;
			    	}).get();
			     $.each( checkedValues, function( key, value ) {
			    	 var listItem = "<li>"+$("#"+value).val()+"</li>";
			    	 $(listItem).appendTo('#customers');
			     });
			  	 
			  	 //Visit Date
			  	 $('#visiDate').text($("#dp").val());
			});
		});
			
	</script>

	<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"><b>Confirm Sales Executive Visit</b></div>
				<div class="modal-body">
					Are you sure you want to schedule the visit for sales executive ?
					<br>
					<br>
					<div>
						<label>Sales Executive Name : </label> <span id="salesEX"></span>
					</div>
					<div>
						<label>Beat Name : </label> <span id="beat"></span>
					</div>
					<div>
						<label> Manufacturers : </label>
						<ul id="manufacturers"></ul>
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
					<!-- a href="#" id="submit" class="btn btn-success success">Submit</a-->
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
</body>

</html>
