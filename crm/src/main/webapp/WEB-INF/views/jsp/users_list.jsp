<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%@ page import="com.sales.crm.model.Role" %>
<html lang="en">

<head>
    <title>Users</title>
    <!-- Bootstrap Core CSS -->
 	<meta charset="utf-8">
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
        <!-- Links -->
        <%@ include file="menus.jsp" %>
        	<div class="row customer_list">
        		<div class="col-md-8">
            		<h2>Users List</h2>   
            	</div>
	        	<div class="col-md-4 add_customer">
	        		<% if(resourcePermIDs.contains(ResourcePermissionEnum.USER_CREATE.getResourcePermissionID())) { %>
						<button type="submit" class="btn btn-primary" onclick="location.href='<%=request.getContextPath()%>/web/userWeb/createUserForm';">Add New User</button>
					<% } %>	
				</div>
			</div>        
            <table class="table table-striped" id="userTable">
                <thead>
                    <tr>
                        <th>User ID</th>
                        <th>User Name</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>EMail ID</th>
                        <th>Mobile No</th>
                        <th>Role</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                	<c:forEach var="user" items="${users}">  
                    <tr>
                    	<% if(resourcePermIDs.contains(ResourcePermissionEnum.USER_READ.getResourcePermissionID())) { %>
                    		<td><a href="<%=request.getContextPath()%>/web/userWeb/${user.userID}">${user.userID}</a></td>
                    	<% } else { %>
                    		<td>${user.userID}</td>
                    	<% } %>	
                        <td>${user.userName}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.emailID}</td>
                        <td>${user.mobileNo}</td>
                        <% String values=""; %>
						<c:forEach var="role" items="${user.roles}">
  								<%
  									if(values.isEmpty()){
  										if((Role)pageContext.getAttribute("role") != null  && ((Role)pageContext.getAttribute("role")).getDescription() != null){
  											values = values+ ((Role)pageContext.getAttribute("role")).getDescription();
  										}
  									}else{
  										values = values + " ,";
  										if((Role)pageContext.getAttribute("role") != null  && ((Role)pageContext.getAttribute("role")).getDescription() != null){
  											values = values+ ((Role)pageContext.getAttribute("role")).getDescription();
  										}
  									}
  								%>
						</c:forEach>
						<td><%= values %></td>
                        <c:if test="${user.statusID == 1}">
   							<td>New</td>
						</c:if>
						<c:if test="${user.statusID == 2}">
   							<td>Active</td>
						</c:if>
						<c:if test="${user.statusID == 3}">
   							<td>Suspended</td>
						</c:if>
                     </tr>
                    </c:forEach>
                </tbody>
            </table>
    </div>
</body>
<script type="text/javascript">
$(document).ready(function() {
    $('#userTable').DataTable({searching: false, aaSorting: [], bLengthChange: false, pageLength: 10});
} );
</script>
</html>
