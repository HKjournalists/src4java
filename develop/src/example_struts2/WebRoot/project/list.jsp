<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>Project List</h1>
	
	<table width="90%" border="2">
	
		<tr >
			<td>Id</td>
			<td>Name</td>
			<td>StratDate</td>
			<td>EndDate</td>
		</tr>
		<c:forEach items=${projectList} var="project">
			<tr>
				<td>${project.id}</td>
				<td>${project.name }</td>
				<td>${project.startDate} </td>
				<td>${project.endDate }</td>
			</tr>
		</c:forEach>
	</table>
	<s:debug></s:debug>
</body>
</html>