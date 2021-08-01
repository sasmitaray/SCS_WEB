<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<%@ page import="com.sales.crm.model.Role" %>
<html lang="en">

<head>
    <title>Areas</title>
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
        <%@ include file="menus.jsp" %>
        <div class="row top-height">
        	<div class="row customer_list">
        		<div class="col-md-8">
            		<h2>Area List</h2>   
            	</div>
	        	<div class="col-md-4 add_customer">
	        		<% if(resourcePermIDs.contains(ResourcePermissionEnum.AREA_CREATE.getResourcePermissionID())) { %>
						<button type="submit" class="btn btn-primary" onclick="location.href='<%=request.getContextPath()%>/web/areaWeb/createAreaForm';">Add New Area</button>
					<% } %>	
				</div>
			</div>        
            <table class="table table-striped" id="areaTable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Word No.</th>
                        <th>Pin Code</th>
                        <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                	<c:forEach var="area" items="${areas}">  
                    <tr>
                    	<td>${area.areaID}</td>
                    	<td>${area.name}</td>
                        <td>${area.description}</td>
                        <td>${area.wordNo}</td>
                        <td>${area.pinCode}</td>
                        <% if(resourcePermIDs.contains(ResourcePermissionEnum.AREA_UPDATE.getResourcePermissionID())) { %>
                        	<td><a href="<%=request.getContextPath()%>/web/areaWeb/editAreaForm/${area.code}">Edit</a>
                        <% } %>		
                        <% if(resourcePermIDs.contains(ResourcePermissionEnum.AREA_DELETE.getResourcePermissionID())) { %>
                        	| 	 
	                        <c:if test="${not empty area.beat}">
	                        	<a href=# id=link data-toggle=modal data-target=#confirm-submit>Delete</a>
	                        </c:if>	
	                        <c:if test="${empty area.beat}">
	                        	<a href=# id=link data-toggle=modal data-target=#confirm data-code=${area.code} data-name=${area.name}>Delete</a>
	                        </c:if>	
	                    <% } %>	    
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
	    $('#areaTable').DataTable({searching: false, aaSorting: [], bLengthChange: false, pageLength: 10});
	} );
</script>



<div class="modal fade" id="confirm-submit" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<b>Warning !</b>
			</div>
			<div class="modal-body">The area can't be removed as this is
				mapped to a beat. Please edit the beat to remove the area
				association, before deleting the area.</div>
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
				<b>Confirm removal of area.</b>
			</div>
			<div class="modal-body">Are you sure you want to remove the
				area, <span style="font-weight:bold;" id="areaName"></span> ?</div>
			<div class="modal-custom-footer">
				<button type="submit" id="modalSubmit" class="btn btn-primary">Confirm</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
				<script type="text/javascript">
					var name = ""
					var id = ""
					$('#confirm').on('show.bs.modal', function (e) {
						$('#areaName').empty();
						code = $(e.relatedTarget).data('code')
						name = $(e.relatedTarget).data('name')
						$('#areaName').append(name);
					});
				
					$('#modalSubmit').click(function(){
						window.location.href = "${contextPath}/web/areaWeb/delete/"+code
					});
				</script>
			</div>
		</div>
	</div>
</div>
</html>
