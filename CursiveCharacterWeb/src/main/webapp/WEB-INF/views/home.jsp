<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<link href="<c:url value="/resources/css/grid.css"/>" rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
<head>
	<title>Home</title>
</head>
<body>

	
	
	<form:form modelAttribute="" action="" class="" enctype="multipart/form-data">
		<fieldset>
			<div>
				<div class="uploadDiv">
				<img src="<c:url value="/resources/images/uploadcharacter.jpg"></c:url>" alt="image"/>
				<button>upload</button>
				<%-- <form:input path="bookImage" type="file" class="form-control"/> --%>
			</div>
			<div class="predictedDiv">
			<img src="<c:url value="/resources/images/predictedcharacter.jpg"></c:url>" alt="image"/>
				
				<button>prediction</button>
				
				<%-- <input type="submit" class="btn btn-primary" value="prediction"/>  --%>
			</div>
			</div>
			
			<%--<div class="card-container">
			
				 <div class="card"></div> 
			</div>--%>
			
		</fieldset>
	</form:form>
		
	
	<h1>
		Hello world!  
	</h1>
	
	<P>  The time on the server is ${serverTime}. </P>
</body>
</html>
