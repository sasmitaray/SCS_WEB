<!DOCTYPE html>
<%@page import="com.sales.crm.model.ManufacturerDelivExecs"%>
<%@page import="com.sales.crm.model.DeliveryExecutive"%>
<%@page import="com.sales.crm.model.SalesExecutive"%>
<%@page import="com.sales.crm.service.SalesExecService"%>
<%@page import="com.sales.crm.model.Manufacturer"%>
<%@page import="com.sales.crm.model.Customer"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%@ page import="com.sales.crm.model.TrimmedCustomer" %>
<%@ page import="com.sales.crm.model.Beat" %>

<html lang="en">

<head>
    <title>Manufacturer Delivery Executives</title>
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
            		<h2>Manufacturer and Delivery Executives</h2>   
            	</div>
            	<div class="col-md-4 add_customer">
            			<button type="submit" class="btn btn-primary"
							onclick="location.href='${contextPath}/web/manufacturerWeb/assignDelivExecutiveForm';">
							Assign Delivery Executives To Manufacturer</button>
				</div>
	        </div>        
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Manufacturer Name</th>
                        <th>Delivery Executives</th>
                       	<th>Action</th>
                    </tr>
                </thead>
                <tbody>
                	<c:forEach var="manufacturerDelivExec" items="${manufacturerDelivExecs}">
                	<%
                		if(((ManufacturerDelivExecs)pageContext.getAttribute("manufacturerDelivExec")).getDelivExecs() !=  null &&
                				((ManufacturerDelivExecs)pageContext.getAttribute("manufacturerDelivExec")).getDelivExecs().size() > 0){
                			
                	%>  
                    <tr>
                   		<td>${manufacturerDelivExec.manufacturer.name}</td>
                        <% String values=""; %>
						<c:forEach var="delivExec" items="${manufacturerDelivExec.delivExecs}">
  								<%
  									if(values.isEmpty()){
  										if((DeliveryExecutive)pageContext.getAttribute("delivExec") != null  && ((DeliveryExecutive)pageContext.getAttribute("delivExec")).getName() != null){
  											values = values+ ((DeliveryExecutive)pageContext.getAttribute("delivExec")).getName();
  										}
  									}else{
  										values = values + ", ";
  										if((DeliveryExecutive)pageContext.getAttribute("delivExec") != null  && ((DeliveryExecutive)pageContext.getAttribute("delivExec")).getName() != null){
  											values = values+ ((DeliveryExecutive)pageContext.getAttribute("delivExec")).getName();
  										}
  									}
  								%>
						</c:forEach>
						<td><%= values %></td>
						<td><a href="${contextPath}/web/manufacturerWeb/assignDelivExecEditForm/${manufacturerDelivExec.manufacturer.code}">Edit</a>
						|
						<c:choose>
							<c:when test = "${manufacturerDelivExec.hasActiveTransaction}">
								<a href=# id=link data-toggle=modal data-target=#confirm-submit>Delete</a>	
							</c:when>
							<c:otherwise>
								<a href=# id=link data-toggle=modal data-code=${manufacturerDelivExec.manufacturer.code} data-target=#confirm>Delete</a></td>
							</c:otherwise>				
						</c:choose>	
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
					<div class="modal-body">The Manufacturer Delivery Executive mapping can not be removed as there are active/completed transactions. 
					                        May be you can try to Edit to remove individual Delivery Executives from manufacturer where there is no active/completed transactions </div>
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
					<b>Confirm removal.</b>
				</div>
				<div class="modal-body">
					Are you sure you want to remove the manufacturer Delivery executive mapping ? .
					Please confirm.
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
							window.location.href = "${contextPath}/web/manufacturerWeb/deleteAassignedDelivExec/" + code  
						});
					</script>
				</div>
			</div>
		</div>
	</div>
</body>

</html>
