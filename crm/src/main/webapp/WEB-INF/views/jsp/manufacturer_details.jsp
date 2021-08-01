<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">

<head>
	<title>Manufacturer Details</title>
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
				<h2>Manufacturer Details</h2>
			</div>
		</div>
		<div class="col-md-8 add_customer">
			<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
				
			<% if(resourcePermIDs.contains(ResourcePermissionEnum.MANUFACTURER_UPDATE.getResourcePermissionID())) { %>
				<button type="submit" class="btn btn-primary"
					onclick="location.href='${contextPath}/web/manufacturerWeb/editManufacturerForm/${manufacturer.code}';">
					Modify</button>
			<% } %>
			
			<% if(resourcePermIDs.contains(ResourcePermissionEnum.MANUFACTURER_DELETE.getResourcePermissionID())) { %>
				<c:choose>
					<c:when test = "${manufacturer.hasTransactions}">
						<a href=# id=link class="btn btn-primary" data-toggle=modal data-target=#confirm-submit>Delete</a>	
					</c:when>
					<c:otherwise>
						<button type="submit" class="btn btn-primary" id="deleteBtn" data-toggle="modal" data-target="#confirm">
									Delete</button>
					</c:otherwise>				
				</c:choose>		
			<% } %>		
		</div>
		<div class="row top-height">
			<div class="col-md-8 ">

				<fieldset>
					<legend>Manufacturer Details</legend>
					<div>
						<label>Name : </label> <span>${manufacturer.name}</span>
					</div>
					<div class="form-group">
						<label>Description :</label> <span>${manufacturer.description}</span>
					</div>
				</fieldset>
				
				<fieldset>
					<legend>Sales Officer</legend>
						<div class="form-group">
							<label>Name : </label> <span>${manufacturer.salesOfficer.name}</span>
						</div>
						<div class="form-group">
							<label>Effective From :</label> <span>${manufacturer.salesOfficer.effectiveFromStr}</span>
						</div>
						<div class="form-group">
							<label>Contact Number :</label> <span>${manufacturer.salesOfficer.contactNo}</span>
						</div>
				</fieldset>
				
				<fieldset>
					<legend>Area Manager</legend>
						<div class="form-group">
							<label>Name : </label> <span>${manufacturer.areaManager.name}</span>
						</div>
						<div class="form-group">
							<label>Effective From :</label> <span>${manufacturer.areaManager.effectiveFromStr}</span>
						</div>
						<div class="form-group">
							<label>Contact Number :</label> <span>${manufacturer.areaManager.contactNo}</span>
						</div>
				</fieldset>

				<fieldset>
					<legend>Main Address</legend>
					<div class="form-group">
						<label>Contact Person :</label> <span>${manufacturer.address[0].contactPerson}</span>
					</div>
					<div class="form-group">
						<label>Address Line 1 :</label> <span>${manufacturer.address[0].addressLine1}</span>
					</div>
					<div class="form-group">
						<label>Address Line 2 :</label> <span>${manufacturer.address[0].addressLine2}</span>
					</div>
					<div class="form-group">
						<label>Street :</label> <span>${manufacturer.address[0].street}</span>
					</div>
					<div class="form-group">
						<label>City :</label> <span>${manufacturer.address[0].city}</span>
					</div>
					<div class="form-group">
						<label>State :</label> <span>${manufacturer.address[0].state}</span>
					</div>
					<div class="form-group">
						<label>Country : </label> <span>${manufacturer.address[0].country}</span>
					</div>
					<div class="form-group">
						<label>Postal Code :</label> <span>${manufacturer.address[0].postalCode}</span>
					</div>
					<div class="form-group">
						<label>Phone Number :</label> <span>${manufacturer.address[0].phoneNumber}</span>
					</div>
					<div class="form-group">
						<label>Mobile Number(Primary) :</label> <span>${manufacturer.address[0].mobileNumberPrimary}</span>
					</div>
					<div class="form-group">
						<label>Mobile Number(Secondary) :</label> <span>${manufacturer.address[0].mobileNumberSecondary}</span>
					</div>
				</fieldset>
				<c:if test="${not empty manufacturer.address[1]}">
					<fieldset>
						<legend>Billing Address</legend>
						<div class="form-group">
							<label>Contact Person :</label> <span>${manufacturer.address[1].contactPerson}</span>
						</div>
						<div class="form-group">
							<label>Address Line 1 :</label> <span>${manufacturer.address[1].addressLine1}</span>
						</div>
						<div class="form-group">
							<label>Address Line 2 :</label> <span>${manufacturer.address[1].addressLine2}</span>
						</div>
						<div class="form-group">
							<label>Street :</label> <span>${manufacturer.address[1].street}</span>
						</div>
						<div class="form-group">
							<label>City :</label> <span>${manufacturer.address[1].city}</span>
						</div>
						<div class="form-group">
							<label>State :</label> <span>${manufacturer.address[1].state}</span>
						</div>
						<div class="form-group">
							<label>Country : </label> <span>${manufacturer.address[1].country}</span>
						</div>
						<div class="form-group">
							<label>Postal Code :</label> <span>${manufacturer.address[1].postalCode}</span>
						</div>
						<div class="form-group">
							<label>Phone Number :</label> <span>${manufacturer.address[1].phoneNumber}</span>
						</div>
						<div class="form-group">
							<label>Mobile Number(Primary) :</label> <span>${manufacturer.address[1].mobileNumberPrimary}</span>
						</div>
						<div class="form-group">
							<label>Mobile Number(Secondary) :</label> <span>${manufacturer.address[1].mobileNumberSecondary}</span>
						</div>
					</fieldset>
				</c:if>
			</div>
		</div>
		<div class="col-md-8 add_customer_buttom">
				<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
				<% if(resourcePermIDs.contains(ResourcePermissionEnum.MANUFACTURER_UPDATE.getResourcePermissionID())) { %>
					<button type="submit" class="btn btn-primary"
						onclick="location.href='${contextPath}/web/manufacturerWeb/editManufacturerForm/${manufacturer.code}';">
						Modify</button>
				<% } %>
				
				<% if(resourcePermIDs.contains(ResourcePermissionEnum.MANUFACTURER_DELETE.getResourcePermissionID())) { %>
					<c:choose>
						<c:when test = "${manufacturer.hasTransactions}">
							<a href=# id=link class="btn btn-primary" data-toggle=modal data-target=#confirm-submit>Delete</a>	
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn btn-primary" id="deleteBtn" data-toggle="modal" data-target="#confirm">
										Delete</button>
						</c:otherwise>				
					</c:choose>		
				<% } %>		
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
					The removal of manufacturer is not allowed as there are Beats or Sales Executives mapped to this manufacturer. 
					You need to remove the Sales Executives from the manufacturer before removing the manufacturer from the system.
				</div>
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
				<div class="modal-header">
					<b>Confirm removal of manufacturer.</b>
				</div>
				<div class="modal-body">
					Are you sure you want to remove the manufacturer, <span><b>${manufacturer.name}</b></span>
					?
				</div>
				<div class="modal-custom-footer">
					<button type="submit" id="modalSubmit" class="btn btn-primary">Confirm</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					<script type="text/javascript">
						$('#modalSubmit').click(function(){
						   window.location.href = "${contextPath}/web/manufacturerWeb/delete/${manufacturer.manufacturerID}"
						});
					</script>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
