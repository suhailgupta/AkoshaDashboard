<%@page import="facebook4j.Facebook;"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:fb="http://ogp.me/ns/fb#">
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="pragma" content="no-cache" /> -->
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath() %>/resources/app.css">

<script type="text/javascript"
	src="<c:url value="/resources/jquery.js" /> "></script>
<script type="text/javascript"
	src="<c:url value="/resources/json.min.js" /> "></script>
<script src="https://connect.facebook.net/en_US/all.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<c:url value="/resources/status.js"/>"></script>
<script type="text/javascript">
	var contexPath = "<%=request.getContextPath() %>";
	var access_token="<%=session.getAttribute("access_token")%>";
	var facebook_loginname="${facebook.name}";
	var logout=contexPath+"/logout";
	var facebook_id="${facebook.id}";

	
	window.onload=function checkSessionExistsOrNot(){
		
		
		$.ajax({  
			type: "GET",  
			url: "sessioncheck?"+Math.random(),
			
			success: function(response){
				
				
			if(response=="notloggedin")
				{
				window.location.href=contexPath+"/signin";
			//	window.location.href="http://www.google.com";
				}
				
				
			},
			error: function(e){  

				console.log("Facebook data could not be retrieved.  Failed with a status of " + status);
			} 
		});


	}
		</script>

</head>
<body>

	<div id="errors">


		<div id="insideerrors">
			<div id="errorMessage">No Such Post/Page exists.</div>
			<div>
				<img id="closeimg" src="http://search.abi.org/icons/closeButton.gif"
					onclick="removeErrorMessages();">
			</div>
		</div>

	</div>
	<div id="LoadingBar">
		<img id="loadingimage"
			src="http://superrembo.com/myicons/FireFoxLoading.gif">
	</div>


	<h2>Welcome to Akosha Dashboard</h2>
	<h4>${facebook.name}</h4>
	<br />
	<div id="container">
		<p>Enter the Brand Page name:</p>
		<div id="getPosts">
			<input type="text" id="brand" name="brand" />
		</div>

		<br /> <input type="button" value="Get Posts"
			onclick="doAjaxGet();getposttimer()">

		<div id="logout">
			<a id="logOut" href=logout>LogOut</a>
		</div>

		<div id="statusandcomments"></div>
	</div>

</body>
</html>