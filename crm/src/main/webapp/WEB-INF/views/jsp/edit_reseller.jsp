<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">

<head>
	<title>Page one</title>
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

.form_submit{
margin-top:14px;
text-align:right;
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
		<div class="row top-height">
			<div class="col-md-8 ">
				<h2>Edit Reseller</h2>
				<form:form modelAttribute="reseller" method="post"
					action="${contextPath}/web/resellerWeb/update">
					<fieldset>
						<legend>Reseller Details</legend>
						<div class="form-group">
							<label>Name</label>
							<form:input name="name" cssClass="form-control" path="name" value="${reseller.name}"/>
						</div>
						<div class="form-group">
							<label>Description</label>
							<form:input name="description" cssClass="form-control"
								path="description" value = "${reseller.description}"/>
						</div>
						<form:hidden name="resellerID" path="resellerID" value="${ reseller.resellerID }"/>
						<form:hidden name="code" path="code" value="${ reseller.code }" />
						<form:hidden name="statusID" path="statusID" value="${ reseller.statusID }" />
					</fieldset>

					<fieldset>
						<legend>Reseller Main Address</legend>
						<form:hidden name="addrressType" value="1"
							path="address[0].addrressType" />
						<form:hidden name="id" value="${ reseller.address[0].id }"
							path="address[0].id" />	
						<div class="form-group">
							<label>Contact Person</label>
							<form:input name="contactPerson" cssClass="form-control"
								path="address[0].contactPerson" value="${reseller.address[0].contactPerson}" />
						</div>
						<div class="form-group">
							<label>Address Line 1</label>
							<form:input name="addressLine1" cssClass="form-control"
								path="address[0].addressLine1" value="${ reseller.address[0].addressLine1 }"/>
						</div>
						<div class="form-group">
							<label>Address Line 2</label>
							<form:input name="addressLine2" cssClass="form-control"
								path="address[0].addressLine2" value="${ reseller.address[0].addressLine2 }"/>
						</div>
						<div class="form-group">
							<label>Street</label>
							<form:input name="street" cssClass="form-control"
								path="address[0].street" value="${ reseller.address[0].street }"/>
						</div>
						<div class="form-group">
							<label>City</label>
							<form:input name="city" cssClass="form-control"
								path="address[0].city" value="${ reseller.address[0].city }"/>
						</div>
						<div class="form-group">
							<label>State</label>
							<form:input name="state" cssClass="form-control"
								path="address[0].state" value="${ reseller.address[0].state }"/>
						</div>
						<div class="form-group">
							<label>Country</label>
							<form:input name="country" cssClass="form-control"
								path="address[0].country" value="${ reseller.address[0].country }"/>
						</div>
						<div class="form-group">
							<label>Postal Code</label>
							<form:input name="postalCode" cssClass="form-control"
								path="address[0].postalCode" value ="${ reseller.address[0].postalCode }"/>
						</div>
						<div class="form-group">
							<label>Phone Number</label>
							<form:input name="phoneNumber" cssClass="form-control"
								path="address[0].phoneNumber" value="${ reseller.address[0].phoneNumber }"/>
						</div>
						<div class="form-group">
							<label>Mobile Number(Primary)</label>
							<form:input name="mobileNumberPrimary" cssClass="form-control"
								path="address[0].mobileNumberPrimary" value="${ reseller.address[0].mobileNumberPrimary }"/>
						</div>
						<div class="form-group">
							<label>Mobile Number(Secondary)</label>
							<form:input name="mobileNumberSecondary" cssClass="form-control"
								path="address[0].mobileNumberSecondary" value="${ reseller.address[0].mobileNumberSecondary }"/>
						</div>
						<form:hidden name="tenantID" path="address[0].tenantID"
							value="${ reseller.address[0].tenantID }" />
						<form:hidden name="code" path="address[0].code" value="${ reseller.address[0].code }" />
						<form:hidden name="statusID" path="address[0].statusID" value="${ reseller.address[0].statusID }" />
						<div>
							<fmt:formatDate value="${ reseller.address[0].dateCreated }" type="date"
								pattern="dd-MM-yyyy" var="createdDate" />
							<form:hidden path="address[0].dateCreated" value="${createdDate}" />
						</div>
						<div>
							<fmt:formatDate value="${ reseller.address[0].dateModified }" type="date"
								pattern="dd-MM-yyyy" var="modifiedDate" />
							<form:hidden path="address[0].dateModified" value="${modifiedDate}" />
						</div>
					</fieldset>
					<fieldset>
						<legend>Reseller Billing Address</legend>
						<form:hidden name="addrressType" value="2"
							path="address[1].addrressType" />
						<form:hidden name="id" value="${ reseller.address[1].id }"
							path="address[1].id" />	
						<div class="form-group">
							<label>Contact Person</label>
							<form:input name="contactPerson" cssClass="form-control"
								path="address[1].contactPerson" value="${ reseller.address[1].contactPerson }"/>
						</div>
						<div class="form-group">
							<label>Address Line 1</label>
							<form:input name="addressLine1" cssClass="form-control"
								path="address[1].addressLine1" value="${ reseller.address[1].addressLine1 }"/>
						</div>
						<div class="form-group">
							<label>Address Line 2</label>
							<form:input name="addressLine2" cssClass="form-control"
								path="address[1].addressLine2" value="${ reseller.address[1].addressLine2 }"/>
						</div>
						<div class="form-group">
							<label>Street</label>
							<form:input name="street" cssClass="form-control"
								path="address[1].street" value="${ reseller.address[1].street }"/>
						</div>
						<div class="form-group">
							<label>City</label>
							<form:input name="city" cssClass="form-control"
								path="address[1].city" value="${ reseller.address[1].city }"/>
						</div>
						<div class="form-group">
							<label>State</label>
							<form:input name="state" cssClass="form-control"
								path="address[1].state" value="${ reseller.address[1].state }"/>
						</div>
						<div class="form-group">
							<label>Country</label>
							<form:input name="country" cssClass="form-control"
								path="address[1].country" value= "${ reseller.address[1].country }"/>
						</div>
						<div class="form-group">
							<label>Postal Code</label>
							<form:input name="postalCode" cssClass="form-control"
								path="address[1].postalCode" value="${ reseller.address[1].postalCode }"/>
						</div>
						<div class="form-group">
							<label>Phone Number</label>
							<form:input name="phoneNumber" cssClass="form-control"
								path="address[1].phoneNumber" value="${ reseller.address[1].phoneNumber }"/>
						</div>
						<div class="form-group">
							<label>Mobile Number(Primary)</label>
							<form:input name="mobileNumberPrimary" cssClass="form-control"
								path="address[1].mobileNumberPrimary" value="${ reseller.address[1].mobileNumberPrimary }"/>
						</div>
						<div class="form-group">
							<label>Mobile Number(Secondary)</label>
							<form:input name="mobileNumberSecondary" cssClass="form-control"
								path="address[1].mobileNumberSecondary" value="${ reseller.address[1].mobileNumberSecondary }"/>
						</div>
						<form:hidden name="tenantID" path="address[1].tenantID"
							value="${ reseller.address[1].tenantID }" />
						<form:hidden name="code" path="address[1].code" value="${ address[1].code }" />
						<form:hidden name="statusID" path="address[1].statusID" value="${ reseller.address[1].statusID }" />
						<div>
							<fmt:formatDate value="${ reseller.address[1].dateCreated }" type="date"
								pattern="dd-MM-yyyy" var="createdDate" />
							<form:hidden path="address[1].dateCreated" value="${createdDate}" />
						</div>
						<div>
							<fmt:formatDate value="${ reseller.address[1].dateModified }" type="date"
								pattern="dd-MM-yyyy" var="modifiedDate" />
							<form:hidden path="address[1].dateModified" value="${modifiedDate}" />
						</div>
					</fieldset>
					<div>
						<fmt:formatDate value="${ reseller.dateCreated }" type="date"
								pattern="dd-MM-yyyy" var="createdDate" />
						<form:hidden path="dateCreated" value="${createdDate}"/>
					</div>
					<div>
						<fmt:formatDate value="${ reseller.dateModified }" type="date"
								pattern="dd-MM-yyyy" var="modifiedDate" />
						<form:hidden path="dateModified" value="${modifiedDate}"/>
					</div>
					<div>
						<form:hidden path="tenantID" value="${ reseller.tenantID }"/>
					</div>
					<div class="form_submit">
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$('#dp').datepicker({format: 'dd/mm/yyyy'});
</script>

</html>
