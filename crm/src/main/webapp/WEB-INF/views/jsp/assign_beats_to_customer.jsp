<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.sales.crm.model.Beat"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html lang="en">

<head>
	<title>Assign Beats To Customer</title>
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
				<form:form modelAttribute="beat" method="post"
					action="${contextPath}/web/beatWeb/assignBeatsToCustomer" id="beatCustform" name="beatCustform">
					<fieldset>
						<legend>Assign Beats to Customer</legend>
							<div class="form-group required">
								<label class='control-label'>Beats</label>
								<form:select path="beatID" cssClass="form-control"
									id="beats" multiple="false">
									<form:option value="-1" label="--- Select ---" />
									<form:options items="${beats}" itemValue="beatID"
										itemLabel="name" required="required" />
								</form:select>
							</div>
							<label id="beatMsg" style="color:red; font-style: italic; font-weight: normal;">Please select the beat from the list.</label>
							
							<div class="form-group required">
								<label class='control-label'>Customers</label>
								<form:select path="customerIDs" cssClass="form-control" id="customers"
									multiple="true">
								</form:select>
							</div>
							<label id="custMsg" style="color:red; font-style: italic; font-weight: normal;">Please select the customer from the list.</label>
					</fieldset>
					<form:hidden name="tenantID" path="tenantID" id="tenantID" value="${ tenantID }" />
					<div class="form_submit">
						<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
						<button type="button" class="btn btn-primary" id="resetBtn" onclick="location.reload();">Reset</button>
						<button type="submit" class="btn btn-primary" id="submitbtn">Submit</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$("#beats").prop('required',true);
		
		$("#customers").prop('required',true);
		$("#beatMsg").hide();
		$("#custMsg").hide();
		
		$('#beats').change(function() {
			$("#beatMsg").hide();
			$("#custMsg").hide();
			var dataFound = false;
			if($('#beats').val() != -1){
				$.ajax({
						type : "GET",
						url : "${contextPath}/rest/customer/customersNotMappedToBeat/"+$('#tenantID').val(),
						dataType : "json",
						success : function(data) {
							$('#customers').empty();
							$.each(data,function(i,obj) {
								dataFound = true;
								var div_data = "<option value="+obj.customerID+">"+ obj.customerName+ "</option>";
								$(div_data).appendTo('#customers');
						});
					}
				});
			}
			//No data found
			if(!dataFound){
				$('#customers').empty();
				var div_data = "<option value=> No Customers found to map </option>";
				$(div_data).appendTo('#customers');
			}
			});
		
		$('#submitbtn').click(function(e){
		     $("#custMsg").hide();
			 $("#beatMsg").hide();
		     if($('#customers').val() == -1){
		    	 $("#custMsg").show();
		    	 e.preventDefault();
		     }
		     if($('#beats').val() == "" || $('#beats').val() == -1){
		    	 $("#beatMsg").show();
		    	 e.preventDefault();
		     }
		   $('#myForm').submit();
		});
	});
	
</script>
</html>
