<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">

<head>
<title>Edit Area</title>
<!-- Bootstrap Core CSS -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
<script
	src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>

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
	content: "*";
	color: red;
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
		<div class="row top-height">
			<div class="col-md-8 ">
				<h2>Edit Area</h2>
				<form:form modelAttribute="area" method="post"
					action="${contextPath}/web/areaWeb/update">
					<fieldset>
						<legend>Area Details</legend>
						<div class="form-group required">
							<label class='control-label'>Area Name</label>
							<form:input name="name" cssClass="form-control" path="name"
								value="${ area.name }" />
						</div>
						<div class="form-group">
							<label>Description</label>
							<form:input name="description" cssClass="form-control"
								path="description" value="${ area.description }" />
						</div>
						<div class="form-group">
							<label>Word Number</label>
							<form:input name="wordNo" cssClass="form-control" path="wordNo"
								value="${ area.wordNo }" />
						</div>
						<div class="form-group">
							<label>PIN Code</label>
							<form:input name="pinCode" cssClass="form-control" path="pinCode"
								value="${ area.pinCode }" />
						</div>
						<form:hidden path="areaID" name="areaID" value="${ area.areaID }" />
						<form:hidden path="tenantID" name="tenantID"
							value="${ area.tenantID }" />
						<form:hidden path="statusID" name="statusID" value="${ area.statusID }" />	
					</fieldset>
					<div>
						<fmt:formatDate value="${ area.dateCreated }" type="date"
							pattern="dd/MM/yyyy" var="createdDate" />
						<form:hidden path="dateCreated" value="${createdDate}" />
					</div>
					<div>
						<fmt:formatDate value="${ area.dateModified }" type="date"
							pattern="dd/MM/yyyy" var="modifiedDate" />
						<form:hidden path="dateModified" value="${modifiedDate}" />
					</div>
					<div>
						<form:hidden name="tenantID" path="tenantID"
							value="${ area.tenantID }" />
						<form:hidden name="code" path="code" value="${ area.code }" />
					</div>
					<div class="form_submit">
						<button type="button" class="btn btn-primary" id="cancelbtn" onclick="window.history.back(); return false;"">Cancel</button>
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
	   		$("#name").prop('required',true);
		});
	</script>
</body>

</html>
