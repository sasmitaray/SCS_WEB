<%@page import="com.sales.crm.model.Beat"%>
<%@page import="com.sales.crm.model.Role"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.sales.crm.model.Area"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en">

<head>
	<title>Beat Details</title>
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
		<%@ include file="menus.jsp" %>
		<div class="row customer_list">
			<div class="col-md-4">
				<h2>Beat Details</h2>
			</div>
		</div>	
		<div class="col-md-8 add_customer">
			<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
			<% if(resourcePermIDs.contains(ResourcePermissionEnum.BEAT_UPDATE.getResourcePermissionID())) { %>
				<button type="submit" class="btn btn-primary"
					onclick="location.href='${contextPath}/web/beatWeb/editBeatForm/${beat.code}';">
					Modify</button>
			<% } %>		
			
			<% if(resourcePermIDs.contains(ResourcePermissionEnum.BEAT_DELETE.getResourcePermissionID())) { %>		
				<button type="submit" class="btn btn-primary" id="deleteButton">
					Delete</button>	
			<% } %>	
		</div>
		<div class="row top-height">
			<div class="col-md-8 ">
				<fieldset>
					<legend>Beat Details</legend>
					<div class="form-group">
						<label>Beat Name : </label> <span>${ beat.name }</span>
					</div>
					<div class="form-group">
						<label>Description : </label> <span>${ beat.description }</span>
					</div>
					<div class="form-group">
						<label>Coverage Schedule : </label> <span>${ beat.coverageSchedule }</span>
					</div>
					<div class="form-group">
						<label>Distance : </label> <span>${ beat.distance }</span>
					</div>
				</fieldset>
				<fieldset>
					<legend>Area Covered</legend>
					<div class="form-group">
						<label>Areas : </label>
						<%
							String values = "";
						%>
						<c:forEach var="area" items="${beat.areas}">
							<%
								if (values.isEmpty()) {
										if ((Area) pageContext.getAttribute("area") != null
												&& ((Area) pageContext.getAttribute("area")).getName() != null) {
											values = values + ((Area) pageContext.getAttribute("area")).getName();
										}
									} else {
										values = values + ", ";
										if ((Area) pageContext.getAttribute("area") != null
												&& ((Area) pageContext.getAttribute("area")).getName() != null) {
											values = values + ((Area) pageContext.getAttribute("area")).getName();
										}
									}
							%>
						</c:forEach>
						<c:if test="${fn:length(beat.areas) gt 0}">
							<span><%=values%></span>
						</c:if>
						<c:if test="${fn:length(beat.areas) eq 0}">
							<span>None</span>
						</c:if>
					</div>
					<% if(((Beat)request.getAttribute("beat")).getAreaIDs() != null && ((Beat)request.getAttribute("beat")).getAreaIDs().size() > 0) { %>
						<input type="hidden" name="area" value="true" id="area">
					<% } %>
					
					<% if(((Beat)request.getAttribute("beat")).getCustomerIDs() != null && ((Beat)request.getAttribute("beat")).getCustomerIDs().size() > 0) { %>
						<input type="hidden" name="customers" value="true" id="customers">
					<% } %>
					
				</fieldset>
			</div>
		</div>
		<div class="col-md-8 add_customer_buttom">
				<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
				<% if(resourcePermIDs.contains(ResourcePermissionEnum.BEAT_UPDATE.getResourcePermissionID())) { %>
					<button type="submit" class="btn btn-primary"
						onclick="location.href='${contextPath}/web/beatWeb/editBeatForm/${beat.code}';">
						Modify</button>
				<% } %>		
				
				<% if(resourcePermIDs.contains(ResourcePermissionEnum.BEAT_DELETE.getResourcePermissionID())) { %>		
					<button type="submit" class="btn btn-primary" id="deleteButton">
						Delete</button>	
				<% } %>	
			</div>
	</div>
</body>
<script type="text/javascript">
	$('#deleteButton').click(function() {
		var message1 = "";
		var message2 = "";
		var status = false;
	    if($('#area').val() == "true"){
	    	status = true;
	    	message1 = "The beat can't be removed as there are areas attached. Please edit the beat to remove the area	association, before deleting the beat.";
	    	$('#confirm-submit').modal('show'); 
	    }
	    if($('#customers').val() == "true"){
	    	status = true;
	    	message2 = "The beat can't be removed as customers are assigned to this. Please remove the beat customer association, before deleting the beat.";
	    }
	    
	    if(status == true){
			$('#msg1').text(message1);
			$('#msg2').text(message2);
			$('#confirm-submit').modal('show'); 
		}else{
			$('#confirm').modal('show'); 
	    }
	 });

</script>

<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<b>Warning !</b>
			</div>
			<div class="modal-body"><span id="msg1"></span><br><span id="msg2"></span></div>
			<div class="modal-custom-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirm" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"><b>Confirm removal of beat.</b></div>
				<div class="modal-body">
					Are you sure you want to remove the beat, <span><b>${ beat.name }</b></span> ?
				</div>
				<div class="modal-custom-footer">
					<button type="submit" id="modalSubmit" class="btn btn-primary">Confirm</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					<script type="text/javascript">
						$('#modalSubmit').click(function(){
						   window.location.href = "${contextPath}/web/beatWeb/delete/${beat.beatID}"
						});
					</script>
				</div>
			</div>
		</div>
	</div>
	
	
</html>
