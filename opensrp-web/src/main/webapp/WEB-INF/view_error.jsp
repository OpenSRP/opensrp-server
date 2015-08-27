<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  
    
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
				<li >
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
		
	<table style="width:100%" class="table" >
		<form:form  class="form-horizontal" 
		method="POST" action="/errorhandler/update_errortrace" modelAttribute="errorTraceForm">
  <tr>
  
    <th>Name:</th>
    <td>
    <spring:bind path="errorTrace.errorType">
    <form:input type="text" path="errorTrace.errorType" name="errorType" readonly="true"/>
    </spring:bind>
    </td>
  </tr>
     <tr>
    <th>Record Id</th>
    <td>
     <spring:bind path="errorTrace.recordId">
    
    <form:input type="text" path="errorTrace.recordId" id="recordId" name="recordId" readonly="true" />
    </spring:bind>
    </td>
  </tr>
 
	    <tr>
    <th>Occurred Date</th>
    <td>
     <fmt:formatDate pattern="yyyy-MM-dd" 
            value="${errorTraceForm.getErrorTrace().dateOccurred}" var="dateString"/>
             <spring:bind path="errorTrace.dateOccurred">
    <form:input type="date" path="errorTrace.dateOccurred" value="${dateString}" id="dateOccurred" name="dateOccurred" readonly="true"  />
   </spring:bind>
    </td>
  </tr>
 	<form:input path="errorTrace.id" type="hidden" value="${errorTrace.getId()}"   />
    <tr>
    <th>Stack Trace</th>
    <td>
     <spring:bind path="errorTrace.stackTrace">
    <form:textarea rows="20" cols="100" path="errorTrace.stackTrace" readonly="true" />
    </spring:bind>
    </td>
  </tr>
    <tr>
    <th>Status</th>
    <td>

      <spring:bind path="errorTrace.status">
    <form:select path="errorTrace.status" >
                  <c:forEach var="option" items="${errorTraceForm.statusOptions}">

                    <form:option value="${option}" label="${option}" />
                  </c:forEach>

                </form:select>
                </spring:bind>
    </td>
  </tr>
 	<tr>
 	<th>URL </th>
    <td>
     <spring:bind path="errorTrace.retryUrl">
    <form:input type="text" path="errorTrace.retryUrl" readonly="true" />
    </spring:bind>
     </td>
 	</tr>
    <tr>
    <th>Action</th>
    <td><a class="btn btn-primary btn-md" role="button" href="${errorTrace.retryUrl}">Retry</a>
	
  
     <input class="btn btn-success btn-md" role="button"  type="submit" value="Update"/>
     </td>
  	</tr>
  
  
   
  </form:form>
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
