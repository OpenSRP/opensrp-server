<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Error Handling</title>


<script type="text/javascript"
	src="<c:url value='/resources/js/jquery.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/js/jquery.dataTables.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/resources/js/bootstrap.min.js'/>"></script>


<link href="<c:url value='/resources/css/jquery.dataTables.min.css'/>"
	rel="stylesheet">
<link href="<c:url value='/resources/css/bootstrap.min.css'/>"
	rel="stylesheet">
<link href="<c:url value='/resources/css/style.css'/>" rel="stylesheet">


<script type="text/javascript" language="JAVASCRIPT" class="init">

var content =new String("<%=request.getContextPath()%>");
var statusOptions =${statusOptions};
var table;

function viewError(id) {
	
	$.get( content+"/errorhandler/viewerror?id="+id, function( data , status) {
		
		$("#errorType").val(data.errorTrace.errorType);  
		$("#documentType").val(data.errorTrace.documentType);
		var d = new Date(data.errorTrace.dateOccurred);
 	    var curr_date = d.getDate();
 	    var curr_month = d.getMonth() + 1; //Months are zero based
 	    var curr_year = d.getFullYear();
		$("#dateOccurred").val(curr_date+"-"+curr_month+"-"+curr_year); 
		$("#stackTrace").val(data.errorTrace.stackTrace);
		$("#retryURL").val(data.errorTrace.retryURL);  
		$('#myModal').modal('show')
		});
}
function getErrors(status){
	
			if(table!=null){
				table.destroy();
			}
				// start of all error method for data table.
				rows_selected = [];	
				table =$('#errorTable').DataTable({
					"ajax" : {
						"url" : content+"/errorhandler/errortrace?status="+status,
						"dataSrc" : ""
					},
					"columns" : [
					{
						"data" : "check"
					}, {
						"data" : "errorType"
					}, {
						"data" : "documentType"
					}, {
						"data" : "recordId",
						"defaultContent": ""
					}, {
						"data" : "dateOccurred",
						 "type": "date"
					}, {
						"data" : "status"
					}, {
						"data": "_id"
					}
					],
					"aoColumnDefs": [
									{
								"mRender": function (data, type, full, meta){
		 					   return '<center><input type="checkbox"></center>';
								},
								"aTargets":[0]
								},
					     			{
					      "mRender":function ( data, type, full ) {
					    	  var d = new Date(data);
					    	    var curr_date = d.getDate();
					    	    var curr_month = d.getMonth() + 1; //Months are zero based
					    	    var curr_year = d.getFullYear();
					    	    //console.log(curr_date + "-" + curr_month + "-" + curr_year);
					    	  
					    	  return curr_date + "-" + curr_month + "-" + curr_year;
					      },
		        			"aTargets": [4]
					      
					},{
						"mRender": function ( data, type, full ) {
				    //   return '<a data-toggle="modal" class="btn btn-info" href="/errorhandler/viewerror?id='+data+'" data-target="#myModal">Click me !</a>';
							return "<button  type='button' class='btn btn-primary'  onclick='viewError(&quot;"+data+"&quot;) ;' >View StackTrace</button>";
				      },
		    				"aTargets": [6]
				},
				{"mRender": function ( data, type, full )
					{
						return "<center>"+data+"</center>"
				},"aTargets": [3]
					},
				{"mRender": function ( data, type, full )
				{
					return "<center>"+data+"</center>"
			}
				}]
});
				
			}

		function updateDataTableSelectAllCtrl(table){
			   var $table             = table.table().node();
			   var $chkbox_all        = $('tbody input[type="checkbox"]', $table);
			   var $chkbox_checked    = $('tbody input[type="checkbox"]:checked', $table);
			   var chkbox_select_all  = $('thead input[name="select_all"]', $table).get(0);

			   // If none of the checkboxes are checked
			   if($chkbox_checked.length === 0){
			      chkbox_select_all.checked = false;
			      if('indeterminate' in chkbox_select_all){
			         chkbox_select_all.indeterminate = false;
			      }

			   // If all of the checkboxes are checked
			   } else if ($chkbox_checked.length === $chkbox_all.length){
			      chkbox_select_all.checked = true;
			      if('indeterminate' in chkbox_select_all){
			         chkbox_select_all.indeterminate = false;
			      }

			   // If some of the checkboxes are checked
			   } else {
			      chkbox_select_all.checked = true;
			      if('indeterminate' in chkbox_select_all){
			         chkbox_select_all.indeterminate = true;
			      }
			   }
			}
		
	//called when 
	$(document).ready(function(e) {

			getErrors('');

			   // Handle click on checkbox
			   $('#errorTable tbody').on('click', 'input[type="checkbox"]', function(e){
			      var $row = $(this).closest('tr');

			      // Get row data
			      var data = table.row($row).data();

			      // Get row ID
			      var rowId = data[0];

			      // Determine whether row ID is in the list of selected row IDs 
			      var index = $.inArray(rowId, rows_selected);

			      // If checkbox is checked and row ID is not in list of selected row IDs
			      if(this.checked && index === -1){
			         rows_selected.push(rowId);

			      // Otherwise, if checkbox is not checked and row ID is in list of selected row IDs
			      } else if (!this.checked && index !== -1){
			         rows_selected.splice(index, 1);
			      }

			      if(this.checked){
			         $row.addClass('selected');
			      } else {
			         $row.removeClass('selected');
			      }

			      // Update state of "Select all" control
			      updateDataTableSelectAllCtrl(table);

			      // Prevent click event from propagating to parent
			      e.stopPropagation();
			   });

			   // Handle click on table cells with checkboxes
			   $('#errorTable').on('click', 'tbody td, thead th:first-child', function(e){
			      $(this).parent().find('input[type="checkbox"]').trigger('click');
			   });

			   // Handle click on "Select all" control
			   $('thead input[name="select_all"]', table.table().container()).on('click', function(e){
			      if(this.checked){
			         $('#errorTable tbody input[type="checkbox"]:not(:checked)').trigger('click');
			      } else {
			         $('#errorTable tbody input[type="checkbox"]:checked').trigger('click');
			      }

			      // Prevent click event from propagating to parent
			      e.stopPropagation();
			   });

			   // Handle table draw event
			   table.on('draw', function(){
			      // Update state of "Select all" control
			      updateDataTableSelectAllCtrl(table);
			   });
			    
			   // Handle form submission event 
			   $('#frm-example').on('submit', function(e){
			      var form = this;

			      // Iterate over all selected checkboxes
			      $.each(rows_selected, function(index, rowId){
			         // Create a hidden element 
			         $(form).append(
			             $('<input>')
			                .attr('type', 'hidden')
			                .attr('name', 'id[]')
			                .val(rowId)
			         );
			      }); 
			      // Remove added elements
			      $('input[name="id\[\]"]', form).remove();
			      // Prevent actual form submission
			      e.preventDefault();
			   });

			   $( "#all_status" ).change(function(e){
				   var data = table.column( 6 ).data().toArray();
				   var selectedStatus=$("thead").find('#all_status :selected');
				    console.log(selectedStatus.val());
				    i=-1;
				    $.each(data,function(){
				    	i=i+1;
				    	var searchIDs = $("#errorTable input:checkbox:checked").map(function(){
				    	      return $(this).val();
				    	    }).get();
				    	console.log("before if i: "+ searchIDs[i+1]);
			    	    if(searchIDs[i+1]=="on")
					   	{
				    	console.log("i: "+ data[i]);
				    	if(!(selectedStatus.text()==""))
				    	$.get( content+"/errorhandler/update_status?id="+data[i]+"&status="+selectedStatus.val(), function( data , status) {
				    	});
					    	}
						    });
				    //console.log(data); // Prevent click event from propagating to parent
				      e.stopPropagation();
				      setInterval(2000);
					  window.location.reload();

			   });
	});
</script>
</head>
<body>

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2">
				<img  height="100" width="100" "OPENSRP"
					src="<c:url value='/resources/opensrp_logo.png'/>">
			</div>
			<div class="col-md-8">
				<h2 class="text-center text-success">Error Handling</h2>
			</div>
			<div class="col-md-2"></div>
		</div>
		<div class="row">
		<div class="col-md-2">
				<ul class="nav nav-stacked nav-tabs">
					<li style="cursor:pointer"><a onclick="getErrors('');">All Errors</a></li>
					<li style="cursor:pointer"><a onclick="getErrors('solved');">Solved Errors</a></li>
					<li style="cursor:pointer"><a onclick="getErrors('unsolved');">Unsolved Errors</a></li>
					<li class="dropdown pull-right" style="cursor:pointer"><a href="#"
						data-toggle="dropdown" class="dropdown-toggle">Options<strong
							class="caret"></strong></a>
						<ul class="dropdown-menu">
							<li><a href="#">Logout</a></li>

						</ul></li>
				</ul>
			</div>
			<div class="col-md-10">
				<table id="errorTable" class="display" cellspacing="0" width="100%">
					<thead>
						<tr>
							<th align="left">
							<input name="select_all" value="1" type="checkbox">
							</th>
							<th>Error Type</th>
							<th>Document Type</th>
							<th>Record Id</th>
							<th>Date Occurred</th>
							<th>
							Status
							<select style="float: center; font-size:13px;" id="all_status" >
							<option value="" selected=""></option>
							<option value="solved">Mark as solved</option>
							<option value="unsolved">Mark as unsolved</option>
							<option value="failed">Mark as failed</option>
							<option value="closed">Mark as closed</option>
							<option value="acknowledged">Mark as acknowledged</option>
							</select>
							</th>
							<th>
							Actions
							</th>

						</tr>
					</thead>

					<tfoot>
						<tr>
							<th></th>
							<th>Error Type</th>
							<th>Document Type</th>
							<th>Record Id</th>
							<th>Date Occurred</th>
							<th>Status</th>
							<th>Actions</th>

						</tr>
					</tfoot>
				</table>
				<!--End of the Table  -->

				<!-- Modal -->
				<div class="modal fade" id="myModal" role="dialog">
					<div class="modal-dialog">

						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h2 class="modal-title">
									<b>Error Log</b>
								</h2>
							</div>
							<div class="modal-body">
								<table style="width: 80%" class="table">
									<tr>

										<th>Error Type:</th>
										<td align="center"><input type="text" id="errorType" readonly /></td>
									</tr>
									<tr>
										<th>Document Type:</th>
										<td align="center"><input type="text" id="documentType" readonly /></td>
									</tr>

									<tr>
										<th>Date Occurred:</th>
										<td><input id="dateOccurred" readonly /></td>
									</tr>
									<tr>
										<th>StackTrace:</th>
										<td align="center"><textarea id="stackTrace" rows="20" cols="40"
												readonly></textarea></td>
									</tr>
									<tr>
										<th>URL:</th>
										<td align="center"><input type="text" id="retryURL" readonly /></td>
									</tr>


								</table>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
							</div>
						</div>
						<!-- End of Modal content-->

						<!-- 
			Creating Table for all errors 
				<c:if test="${type=='all'}">


					<c:choose>
						<c:when test="${empty errors}">
							<div class="alert alert-warning">
								<strong>Warning!</strong> No Record Found in Database !
							</div>
						</c:when>
						<c:otherwise>
							<table class="table" id="table">
								<thead>
									<tr>

										<th>Error Type</th>
										<th>Document Type</th>
										<th>Record Id</th>
										<th>Date Occurred</th>

										<th>Status</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="i" items="${errors}">
										<tr>


											<td>${i.errorType}</td>
											<td>${i.documentType}</td>
											<td>${i.recordId}</td>
											<td><fmt:formatDate pattern="dd-MM-yyyy"
													value="${i.dateOccurred}" /></td>
											<td>${i.status}</td>
											<td><a class="btn btn-primary btn-md" role="button"
												href="/errorhandler/viewerror?id=${i.getId()}">View</a>
											<button class="btn btn-primary btn-md" role="button" onclick="viewError('${i.getId()}');">View Data</button>
												</td>
										</tr>
									</c:forEach>

								</tbody>
							</table>
						</c:otherwise>
					</c:choose>
				</c:if>
				 Creating Table for solved errors
				<c:if test="${type == 'solved' }">
					<c:choose>
						<c:when test="${empty errors}">
							<div class="alert alert-warning">
								<strong>Warning!</strong> No Record Found under All !
							</div>
						</c:when>
						<c:otherwise>

							<table class="table">
								<thead>
									<tr>

										<th>Error Type</th>
										<th>Document Type</th>
										<th>Record ID</th>
										<th>Date</th>
										<th>Status</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="i" items="${errors}">
										<tr>


											<td>${i.errorType}</td>
											<td>${i.documentType}</td>
											<td>${i.recordId}</td>
											<td><fmt:formatDate pattern="dd-MM-yyyy"
													value="${i.dateOccurred}" /></td>
											<td>${i.status}</td>
											<td><a class="btn btn-primary btn-md" role="button"
												href="/errorhandler/viewerror?id=${i.getId()}">View</a></td>

										</tr>
									</c:forEach>

								</tbody>
							</table>
						</c:otherwise>
					</c:choose>
				</c:if>

				Creating Table for unsolved errors  
				<c:if test="${type == 'unsolved' }">
					<c:if test="${empty  errors }">
						<div class="alert alert-warning">
							<strong>Warning!</strong> No Record Found under DB !
						</div>
					</c:if>


					<table class="table">
						<thead>
							<tr>

								<th>Error Type</th>
								<th>Document Type</th>
								<th>Record ID</th>
								<th>Date</th>
								<th>Status</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="i" items="${errors}">
								<tr>


									<td>${i.errorType}</td>
									<td>${i.documentType}</td>

									<td>${i.recordId}</td>
									<td><fmt:formatDate pattern="dd-MM-yyyy"
											value="${i.dateOccurred}" /></td>
									<td>${i.status}</td>
									<td><a class="btn btn-primary btn-md" role="button"
										href="/errorhandler/viewerror?id=${i.getId()}">View</a></td>

								</tr>
							</c:forEach>

						</tbody>
					</table>


				</c:if>-->

					</div>
				</div>
				<div class="row">
					<div class="col-md-4"></div>
					<div class="col-md-8"></div>
				</div>
			</div>

			<%-- 	<script src="<c:url value='/resources/js/jquery.min.js' />"></script>
	<script src="<c:url value='/resources/js/bootstrap.min.js'/>"></script>
	<script src="<c:url value='/resources/js/scripts.js'/>"></script> --%>
</body>
</html>
