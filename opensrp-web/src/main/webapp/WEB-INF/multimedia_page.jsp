<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<link href="https://nightly.datatables.net/css/jquery.dataTables.css"
	rel="stylesheet" type="text/css" />
<script src="https://nightly.datatables.net/js/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
 
<link href="<c:url value='/resources/css/style.css'/>" rel="stylesheet">

<script type="text/javascript" language="JAVASCRIPT" class="init">
var content =new String("<%=request.getContextPath()%>");
var table;
	$(document).ready( function () {
		  table= $('#multimediaTable').DataTable({
			  "language": {
			        "search": "_INPUT_",
			        "searchPlaceholder": "Search..."
			    },
			    "paging" : true,
				"lengthChange" : false,
				"searching" : true,
				"ordering" : true,
				"info" : false,
				"autoWidth" : true,
				"sDom" : 'lfrtip',
		        "serverSide": true,
		       	"processing": true,
				"ajax": {
				    "url": content + "/multimediaData?searchByName=&searchByContent=&searchByStartDate=&searchByEndDate=",
				    "dataSrc": ""
				  },
				 "columns": [
				             { "data": function ( row, type, set, meta ){
			return "<a  href=\"/opensrp-web/preview?path="+ row.filePath + "\"><img alt=\""+row.name+"\" style=\"width:200px;height:200px;\" src=\"/opensrp-web/image?path="+ row.filePath + "\"></a>";
					             } },
					             { "data": function ( row, type, set, meta ){
					            	 return "<strong>Multimedia Name: </strong>" + row.name + "<br>"
										+ "<strong>File Size: </strong>" + row.fileSize + "<br>"
										+ "<strong>Category: </strong>" + row.fileCategory+ "<br>"
										+ "<strong>Content Type: </strong>" + row.contentType + "<br>"
										+ "<strong>Case Id: </strong>" + row.caseId + "<br>"
										+ "<strong>Provider Id: </strong>" + row.providerId ;
					     					             } },
					     					            { "data": function ( row, type, set, meta ){
					    					     			return "<strong>Description: </strong>" + row.description +"<br><br>"+
					    									"<a href=\"/opensrp-web/download?path="+ row.filePath + "\" download><button type=\"button\">Download</button></a>"
					    					     					             } }]
			    });

		    $("#submitBtn").click(function(){
		    	alert(table+ " hello: "+$("#searchByName").val());
		    	$('#multimediaTable').DataTable().destroy();
		    	table =	$('#multimediaTable').DataTable({
		    		"dom": '<"top"i>rt<"bottom"flp><"clear">',
		    		"language": {
				        "search": "_INPUT_",
				        "searchPlaceholder": "Search..."
				    },
			    	"paging" : true,
					"lengthChange" : false,
					"searching" : true,
					"ordering" : true,
					"info" : true,
					"autoWidth" : true,
					"sDom" : 'lfrtip',
			        "serverSide": true,
			       	"processing": true,
					"ajax": {
					    "url": content + "/multimediaData?searchByName="+$("#searchByName").val()+"&searchByContent="+$("#searchByContent").val()+"&searchByStartDate="+$("#searchByDate_1").val()+"&searchByEndDate="+$("#searchByDate_2").val(),
					    "dataSrc": ""
					  },
					 "columns": [
					             { "data": function ( row, type, set, meta ){
				
				return "<a  href=\"/opensrp-web/preview?path="+ row.filePath + "\"><img alt=\""+row.name+"\" style=\"width:200px;height:200px;\" src=\"/opensrp-web/image?path="+ row.filePath + "\"></a>";
						             } },
						             { "data": function ( row, type, set, meta ){
						            	 return "<strong>Multimedia Name: </strong>" + row.name + "<br>"
											+ "<strong>File Size: </strong>" + row.fileSize + "<br>"
											+ "<strong>Category: </strong>" + row.fileCategory+ "<br>"
											+ "<strong>Content Type: </strong>" + row.contentType + "<br>"
											+ "<strong>Case Id: </strong>" + row.caseId + "<br>"
											+ "<strong>Provider Id: </strong>" + row.providerId ;
						     					             } },
						     					            { "data": function ( row, type, set, meta ){
						    					     			return "<strong>Description: </strong>" + row.description +"<br><br>"+
						    									"<a href=\"/opensrp-web/download?path="+ row.filePath + "\" download><button type=\"button\">Download</button></a>"
						    					     					             } }]
				    });

		    });

		    $($('#multimediaTable').DataTable().table().container())
		      .find('div.dataTables_paginate')
		      .css( 'display', table.page.info().pages <= 1 ?
		           'none' :
		           'block'
		      )
			} );
	
</script>
</head>
<header>
	<div class="row" style="height: 20%;"><center><h1>Multimedia</h1></center></div>
</header>
<body>
	<div class="container-fluid">
			<div class="col-sm-14" id="searchDiv" style="margin: 0 auto;">
					<h4 Style="float: left;">
						Content Type: <select id="searchByContent">
							<option value=""></option>
							<option value="video">Videos</option>
							<option value="image">Images</option>
							<option value="pdf">Documents</option>
						</select>
					</h4>
					<h4 Style="float: left; margin-left: 20px;">
						Multimedia Name:<input type="search" id="searchByName">
					</h4>
					<h4 Style="float: left; margin-left: 20px;">
						Data Range:<input type="date" id="searchByDate_1">
					</h4>
					<h4 Style="float: left; margin-left: 10px;">
						-	<input type="date" id="searchByDate_2">
					</h4>
					<h4 Style="float: left; margin-left: 30px;">
						<input type="button" id="submitBtn" value="Search"
							Style="float: left; margin-left: 30px;">
					</h4>
			</div>
			<div class="col-sm-14">
			<table id="multimediaTable" >
					<thead>
						<tr>
							<th style="width: 20%;">Image</th>
							<th style="width: 30%;">Details</th>
							<th style="width: 50%;">Description</th>
							</tr>
					</thead>
					 <tfoot>
						<tr>
							<th style="width: 20%;">Image</th>
							<th style="width: 30%;">Details</th>
							<th style="width: 50%;">Description</th>
						</tr>
					</tfoot> 
				</table>
			</div>
		</div>
	<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Modal Header</h4>
        </div>
        <div class="modal-body">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
</body>

<footer></footer>

</html>
