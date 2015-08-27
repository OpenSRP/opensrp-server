<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Error Handling</title>
    
<%--     <script type="text/javascript" src="<c:url value="/resources/src/js/jquery.min.js"/>"></script> --%>

  

    <link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">

  </head>
  <body>

    <div class="container-fluid">
	<div class="row">
		<div class="col-md-2">
			<img alt="OPENSRP" src="/resources/opensrp-logo.png">
		</div>
		<div class="col-md-8">
			<h3 class="text-center text-success">
				Error Handling -- OpenSRP
			</h3>
		</div>
		<div class="col-md-2">
		</div>
	</div>
	<div class="row">
		<div class="col-md-3">
			<ul class="nav nav-stacked nav-tabs">
				<li >
					<a href="/errorhandler/errortrace" >All Errors</a>
				</li>
				<li class="active">
					<a href="/errorhandler/solvederrors" >Solved Errors</a>
				</li>
				<li  >
					<a href="/errorhandler/unsolvederrors">Unsolved Errors</a>
				</li>
				<li class="dropdown pull-right">
					 <a href="#" data-toggle="dropdown" class="dropdown-toggle">Options<strong class="caret"></strong></a>
					<ul class="dropdown-menu">
						<li>
							<a href="#">Logout</a>
						</li>
						
					</ul>
				</li>
			</ul>
		</div>
		<div class="col-md-9">
			<table class="table">
				<thead>
					<tr>
				
						<th>
							Name
						</th>
						<th>
							Date
						</th>
						<th>
							Status
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="i" items="${errors}">
				<tr>
  
	
		<td>${i.errorType}</td> 
	<td><fmt:formatDate pattern="dd-MM-yyyy" 
            value="${i.dateOccurred}"/></td> 
	<td>${i.status}</td> 
		<td><a class="btn btn-primary btn-md" role="button" href="/errorhandler/viewerror?id=${i.getId()}" >View</a></td>
	
	</tr>
</c:forEach>
				
				</tbody>
			</table>
		</div>
	</div>
	<div class="row">
		<div class="col-md-4">
		</div>
		<div class="col-md-8">
		</div>
	</div>
</div>

    <script src="<c:url value='/resources/js/jquery.min.js' />"></script>
    <script src="<c:url value='/resources/js/bootstrap.min.js'/>"></script>
    <script src="<c:url value='/resources/js/scripts.js'/>"></script>
  </body>
</html>