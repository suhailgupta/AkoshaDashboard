<%@page contentType="text/html;charset=UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
	<head>

<title><fmt:message key="welcome.title"/></title>
</head>
<body>


<div class="container">  
	<h1>
	
		<fmt:message key="welcome.title"/>
	</h1>
	
	<ul>
		<li><a href="signin" >Login with Facebook user/password</a></li>
		
	</ul>
	
</div>

</body>
</html>