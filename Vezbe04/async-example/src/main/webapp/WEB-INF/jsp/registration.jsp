<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Async Example</title>
<style type="text/css">
html, body {
	height: 100%;
}

body {
	margin: 0;
	background: linear-gradient(45deg, #49a09d, #5f2c82);
	font-family: sans-serif;
	font-weight: 100;
}

.container {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.form-style-4 {
	width: 450px;
	font-size: 16px;
	background: #495C70;
	padding: 30px 30px 15px 30px;
	border: 5px solid #53687E;
}

.form-style-4 input[type=submit], .form-style-4 input[type=button],
	.form-style-4 input[type=text], .form-style-4 input[type=email],
	.form-style-4 textarea, .form-style-4 label {
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: 16px;
	color: #fff;
}

.form-style-4 label {
	display: block;
	margin-bottom: 10px;
}

.form-style-4 label>span {
	display: inline-block;
	float: left;
	width: 150px;
}

.form-style-4 input[type=text], .form-style-4 input[type=email] {
	background: transparent;
	border: none;
	border-bottom: 1px dashed #83A4C5;
	width: 275px;
	outline: none;
	padding: 0px 0px 0px 0px;
	font-style: italic;
}

.form-style-4 textarea {
	font-style: italic;
	padding: 0px 0px 0px 0px;
	background: transparent;
	outline: none;
	border: none;
	border-bottom: 1px dashed #83A4C5;
	width: 275px;
	overflow: hidden;
	resize: none;
	height: 20px;
}

.form-style-4 textarea:focus, .form-style-4 input[type=text]:focus,
	.form-style-4 input[type=email]:focus, .form-style-4 input[type=email] :focus
	{
	border-bottom: 1px dashed #D9FFA9;
}

.form-style-4 input[type=submit], .form-style-4 input[type=button] {
	background: #576E86;
	border: none;
	padding: 8px 10px 8px 10px;
	border-radius: 5px;
	color: #A8BACE;
}

.form-style-4 input[type=submit]:hover, .form-style-4 input[type=button]:hover
	{
	background: #394D61;
}
</style>
</head>
<body>
	<div class="container">
		<c:url var="action" value="/users/signup/async" />
		<form:form class="form-style-4" id="formUserAsync" action="${action}" method="post"
			modelAttribute="user">
				<form:label path="firstname">Firstname </form:label>
				<form:input path="firstname" />
				<form:label path="lastname">Lastname </form:label>
				<form:input path="lastname" />
				<form:label path="emailAddress">Email address </form:label>
				<form:input path="emailAddress" />
			<label> <span> </span><input type="submit" value="Submit" />
			</label>
		</form:form>
		<c:url var="action" value="/users/signup/sync" />
		<form:form class="form-style-4" id="formUserSync" action="${action}" method="post"
			modelAttribute="user">
				<form:label path="firstname">Firstname </form:label>
				<form:input path="firstname" />
				<form:label path="lastname">Lastname </form:label>
				<form:input path="lastname" />
				<form:label path="emailAddress">Email address </form:label>
				<form:input path="emailAddress" />
			<label> <span> </span><input type="submit" value="Submit" />
			</label>
		</form:form>
	</div>
</body>
</html>