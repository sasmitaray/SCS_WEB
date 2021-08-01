<!DOCTYPE html>
<%@page import="com.sales.crm.model.EntityStatusEnum"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>   
<%@page import="com.sales.crm.model.Customer"%> 
<%@page import="com.sales.crm.util.EncodeDecodeUtil"%>
<html lang="en">

<head>
    <title>Customers</title>
    <!-- Bootstrap Core CSS -->
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
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
    
    .upload_input {
    display: block;
    visibility: hidden;
    width: 0;
    height: 0;
	}
	
	table.table.table-striped thead {
    background: #ddd;
    padding: 10px 0 10px 0;
	}
	
	input[type=file] {
    display: inline-block;
 }
 
 .table {
    width: 100%;
    max-width: 100%;
    margin-bottom: 20px;
    margin-top: 10px;
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
       		<div class="col-md-4">
           		<h2>Customers List</h2>   
           	</div>
        	<div class="col-md-8 add_customer">
        		<form id="customerForm">
	        		<% if(resourcePermIDs.contains(ResourcePermissionEnum.CUSTOMER_CREATE.getResourcePermissionID())) { %>
	        			<input type="button" value="Download Template" class="btn btn-primary" id="downloadTmp">
	        			<input type="file" name="file" id="upload" size="chars" class="upload_input" onchange="myFunction()">
	        			<input type="button" value="Upload Customers Excel" class="btn btn-primary" id="uploadBtn">
						<button type="submit" id="newCustomer" class="btn btn-primary">Add New Customer</button>
					<% } %>
				</form>	
			</div>
		</div>
		<div class="row" >
		<form id="searchForm">
			<div class="col-md-2" style="padding-right: 1px;">
				<select id="searchParam" name="searchBy" class="form-control">
					<option value="" label="-Search By-"></option>
					<option value="ID">Customer ID</option>
					<option value="Name">Name</option>
					<option value="City">City</option>
					<option value="BEAT_NAME">Beat</option>
				</select>
			</div>
			<div class="col-md-2" style="padding-right: 1px;">
				<input type="text" class="form-control" id="searchVal"/>
			</div>
			<div class="col-md-2">
				<button type="submit" id="search" class="btn btn-primary" disabled>Search</button>
			</div>
		</form>	
		</div>

		<table class="table table-striped" id="custTable">
            <thead>
                <tr>
                    <th>Customer ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Beat</th>
                    <th>City</th>
                    <th>Contact Person</th>
                    <th>Phone Number</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            	<c:forEach var="customer" items="${customers}">  
            	<%
					String encodedCustID = "";
				%>
            	
                <tr>
                 	<% if(resourcePermIDs.contains(ResourcePermissionEnum.CUSTOMER_READ.getResourcePermissionID())) { 
                 		encodedCustID = EncodeDecodeUtil.encodeNumberToString(((Customer)pageContext.getAttribute("customer")).getCustomerID());
                 	 %>	
                		<td><a href="<%=request.getContextPath()%>/web/customerWeb/${customer.code}">${customer.customerID}</a></td>
                	<% }else{ %>
                		<td>${customer.customerID}</td>
                	<% } %>
                    <td>${customer.name}</td>
                    <td>${customer.description}</td>
                    <td>${customer.beatName}</td>
                    <td>${customer.address[0].city}</td>
                    <td>${customer.address[0].contactPerson}</td>
                    <td>${customer.address[0].phoneNumber}</td>
                    <td>${customer.statusAsString}</td>
                 </tr>
                </c:forEach>
            </tbody>
        </table>
   	</div>
</body>
	<script type="text/javascript">
		
		$(document).ready(function() {
		    $('#custTable').DataTable({searching: false, aaSorting: [], bLengthChange: false, pageLength: 10});
		} );
	
		$('#uploadBtn').click(function(){
			$('#upload').click();
		});
		
		$('#newCustomer').click(function(){
			$('#customerForm').attr('method', "GET");
			$('#customerForm').attr('action', "${contextPath}/web/customerWeb/createCustomerForm").submit();
			
		});
		
		$('#downloadTmp').click(function(){
			$('#customerForm').attr('method', "GET");
			$('#customerForm').attr('action', "${contextPath}/web/customerWeb/downloadAddCustomerTemplate").submit();
			
		});
		
		$('#upload').change(function(){
			$('#customerForm').attr("enctype", "multipart/form-data");
			$('#customerForm').attr('method', "POST");
			$('#customerForm').attr('action', "${contextPath}/web/customerWeb/fileUpload").submit();
		});
		
		$('#searchParam').change(function(){
			if($("#searchParam").prop('selectedIndex') != 0 && $('#searchVal').val()){
				$('#search').prop('disabled', false);
			}else{
				$('#search').prop('disabled', true);
			}
		});
		
		$('#searchVal').blur(function() {
			if($("#searchParam").prop('selectedIndex') != 0 && $('#searchVal').val()){
				$('#search').prop('disabled', false);
			}else{
				$('#search').prop('disabled', true);
			}
		});
		
		$('#search').click(function(){
			$.ajax({
				type : "GET",
				url : "${contextPath}/rest/customer/search/"+$('#searchParam').val()+"/"+$('#searchVal').val(),
				dataType : "json",
				success : function(data) {
					$("#custTable > tbody").empty();
					var result = data.businessEntities;
					$.each(result,function(i,customer) {
						var row_data = "<tr><td>"+customer.customerID+"</td><td>"+customer.name+"</td><td>"+customer.description+"</td><td>"+customer.beatName+"</td><td>"+customer.address[0].city+"</td><td>"+customer.address[0].contactPerson+"</td><td>"+customer.address[0].phoneNumber+"</td></tr>";
		                $("#custTable > tbody").append(row_data);
					});
				}
			});
			return false;
		});
		
	</script>
</html>
