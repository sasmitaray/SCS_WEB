<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%@page import="com.sales.crm.model.Manufacturer"%> 
<%@page import="com.sales.crm.util.EncodeDecodeUtil"%>
<html lang="en">

<head>
    <title>Manufacturers</title>
    <!-- Bootstrap Core CSS -->
	 <meta charset="utf-8">
	<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" />
	<script src="<%=request.getContextPath()%>/resources/js/jquery-3.2.0.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
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
    
    table.table.table-striped thead {
    background: #ddd;
    padding: 10px 0 10px 0;
	}
	
	.dataTables_paginate {
    margin-top: -20px;
    text-align: right;
    float: right;
    display: block
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
            		<h2>Manufacturers List</h2>   
            	</div>
	        	<div class="col-md-4 add_customer">
	        		<% if(resourcePermIDs.contains(ResourcePermissionEnum.MANUFACTURER_CREATE.getResourcePermissionID())) { %>
						<button type="submit" class="btn btn-primary" onclick="location.href='<%=request.getContextPath()%>/web/manufacturerWeb/createManufacturerForm';">Add New Manufacturer</button>
					<% } %>
				</div>
			</div>        
			<table class="table table-striped" id="manufTable">
                <thead>
                    <tr>
                        <th>Manufacturer ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Manufacturer Sales Officer</th>
                        <th>Manufacturer Area Manager</th>
                        <th>City</th>
                    </tr>
                </thead>
                <tbody>
                	<c:forEach var="manufacturer" items="${manufacturers}">  
                	<%
					String encodedManufID = "";
					%>
            	
                    <tr>
                    	<% if(resourcePermIDs.contains(ResourcePermissionEnum.MANUFACTURER_READ.getResourcePermissionID())) { %>
                    		<td><a href="<%=request.getContextPath()%>/web/manufacturerWeb/${manufacturer.code}">${manufacturer.manufacturerID}</a></td>
                    	<% } else { %>	
                    		<td>${manufacturer.manufacturerID}</td>
                    	<%  } %>
                        <td>${manufacturer.name}</td>
                        <td>${manufacturer.description}</td>
                        <td>${manufacturer.salesOfficer.name}</td>
                        <td>${manufacturer.areaManager.name}</td>
                        <td>${manufacturer.address[0].city}</td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
</body>

<script type="text/javascript">
$(document).ready(function() {
    $('#manufTable').DataTable({searching: false, aaSorting: [], bLengthChange: false, pageLength: 10});
} );

</script>

</html>
