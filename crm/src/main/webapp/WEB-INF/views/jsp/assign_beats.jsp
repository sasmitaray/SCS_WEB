<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.sales.crm.model.SalesExecutive"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html lang="en">

<head>
<title>Assign Beats</title>
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
				<form:form modelAttribute="manufSalesExecBeats" method="post"
					action="${contextPath}/web/salesExecWeb/assignBeat">
					<fieldset>
						<legend>Assign Beats to Sales Executive</legend>
						<div class="form-group required">
								<label class='control-label'>Manufacturer</label>
								<form:select path="manufacturer.manufacturerID" cssClass="form-control" id="manufacturers">
									<form:option value="-1" label="--- Select ---" required="required"/>
									<c:forEach var="cmanufacturer" items="${manufacturers}">
										<form:option value="${ cmanufacturer.manufacturerID }" label="${ cmanufacturer.name }" required="required"/>
									</c:forEach>
								</form:select>
						</div>
						<div class="form-group required">
								<label class='control-label'>Sales Executives</label>
								<form:select path="salesExecutive.userID" cssClass="form-control" id="salesExecs"
									multiple="false">
									<form:option value="-1" label="--- Select ---"/>
								</form:select>
						</div>
						<div class="form-group required">
							<label class='control-label'>Beats</label>
							<form:select path="beatIDLists" cssClass="form-control" multiple="true" id="beats">
							</form:select>
						</div>
					</fieldset>
					<form:hidden name="tenantID" path="tenantID" id="tenantID" value="${ tenantID }" />
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
		$("#salesExecs").prop('required',true);
		$("#beats").prop('required',true);
		
		$('#manufacturers').change(function() {
			if($('#manufacturers').val() != -1){
				$.ajax({
						type : "GET",
						url : "${contextPath}/rest/manufacturerReST/salesExecs/"+$('#manufacturers').val()+"/"+$('#tenantID').val(),
						dataType : "json",
						success : function(data) {
							$('#salesExecs').empty();
							var empty_select = "<option value=-1>--- Select ---</option>";
							$(empty_select).appendTo('#salesExecs');
							$.each(data,function(i,obj) {
								var div_data = "<option value="+obj.userID+">"+ obj.name+ "</option>";
								$(div_data).appendTo('#salesExecs');
						});
					}
				});
			}
		});
		
		$('#salesExecs').change(function() {
			var dataFound = false;
			if($('#salesExecs').val() != -1){
				$.ajax({
						type : "GET",
						url : "${contextPath}/rest/beatReST/beatsNotMappedToSalesExec/"+$('#manufacturers').val() +"/"+$('#salesExecs').val()+"/"+$('#tenantID').val(),
						dataType : "json",
						success : function(data) {
							$('#beats').empty();
							$.each(data,function(i,obj) {
								dataFound = true;
								var div_data = "<option value="+obj.beatID+">"+ obj.name+ "</option>";
								$(div_data).appendTo('#beats');
						});
					}
				});
			}
			
			//No data found
			if(!dataFound){
				$('#beats').empty();
				var div_data = "<option value=> No Beats to map </option>";
				$(div_data).appendTo('#beats');
			}
		});
	});
</script>
</html>
