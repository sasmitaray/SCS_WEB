<!DOCTYPE html>
<%@page import="com.sales.crm.model.Customer"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%@ page import="com.sales.crm.model.TrimmedCustomer" %>
<%@ page import="com.sales.crm.model.Beat" %>

<html lang="en">

<head>
    <title>Beat customers</title>
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
	
	.modal-custom-footer {
    padding: 15px;
    text-align: center;
    border-top: 1px solid #e5e5e5;
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
            		<h2>Beats and Customers</h2>   
            	</div>
            	<div class="col-md-4 add_customer">
            		<% if(resourcePermIDs.contains(ResourcePermissionEnum.BEAT_ASSOCIATE_CUSTOMERS.getResourcePermissionID())) { %>
						<button type="submit" class="btn btn-primary"
							onclick="location.href='${contextPath}/web/beatWeb/assignBeatsForm';">
							Assign Beat To Customer</button>
					<% } %>	
				</div>
	        </div>        
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Beat Name</th>
                        <th>Customers</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                	<c:forEach var="beat" items="${beats}">
                	<%
                		if(((Beat)pageContext.getAttribute("beat")).getCustomers() !=  null &&
                				((Beat)pageContext.getAttribute("beat")).getCustomers().size() > 0){
                			
                	%>  
                    <tr>
                   		<td>${beat.name}</td>
                        <% String values=""; %>
						<c:forEach var="customer" items="${beat.customers}">
  								<%
  									if(values.isEmpty()){
  										if((TrimmedCustomer)pageContext.getAttribute("customer") != null  && ((TrimmedCustomer)pageContext.getAttribute("customer")).getCustomerName() != null){
  											values = values+ ((TrimmedCustomer)pageContext.getAttribute("customer")).getCustomerName();
  										}
  									}else{
  										values = values + ", ";
  										if((TrimmedCustomer)pageContext.getAttribute("customer") != null  && ((TrimmedCustomer)pageContext.getAttribute("customer")).getCustomerName() != null){
  											values = values+ ((TrimmedCustomer)pageContext.getAttribute("customer")).getCustomerName();
  										}
  									}
  								%>
						</c:forEach>
						<td><%= values %></td>
						<% if(resourcePermIDs.contains(ResourcePermissionEnum.BEAT_EDIT_ASSOCIATED_CUSTOMERS.getResourcePermissionID())) { %>
							<td><a href="${contextPath}/web/beatWeb/assignedBeatCustomerEditForm/${beat.code}">Edit</a>
						<% } %>	
						
						<% if(resourcePermIDs.contains(ResourcePermissionEnum.BEAT_DELETE_ASSOCIATED_CUSTOMERS.getResourcePermissionID())) { %>
							|
							<c:choose>
								<c:when test = "${beat.hasBeatCustomersTransaction}">
									<a href=# id=link data-toggle=modal data-target=#confirm-submit>Delete</a>	
								</c:when>
								<c:otherwise>
									<a href=# id=link id="deleteBtn" data-code=${beat.code} data-toggle="modal" data-target="#confirm">Delete</a></td>
								</c:otherwise>				
							</c:choose>	
						<% } %>	
                    </tr>
                    <% 
                		}
                    %>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<b>Warning !</b>
					</div>
					<div class="modal-body">The Manufacturer Beat mapping can not be removed as there are active/completed transactions. 
					                        May be you can try to Edit to remove beats from manufacturer where there is no active/completed transactions </div>
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
					<b>Confirm removal !</b>
				</div>
				<div class="modal-body">
					Are you sure you want to remove the customer beat mapping ? Please confirm.
				</div>
				<div class="modal-custom-footer">
					<button type="submit" id="delete" class="btn btn-primary">Confirm</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
					<script type="text/javascript">
						var code = "";
						$('#confirm').on('show.bs.modal', function (e) {
							code = $(e.relatedTarget).data('code')
						});
						//Click on confirm button
						$('#delete').click(function(e){
							window.location.href = "${contextPath}/web/beatWeb/deleteAssignedCustomersBeatLink/" + code
						});
					</script>
				</div>
			</div>
		</div>
	</div>
</body>

</html>
