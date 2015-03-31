<%@page import="model.Document"%>
<%@page import="servlets.SearchServlet"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Azure Search - Feature Search</title>
</head>
<body>
<body>
	<h2>USGS Features Search for Washington State</h2>
	<form action="SearchServlet" method="POST">
		<input type="text" name="SearchQuery" size="100" > 
		<input type="submit" value="Search" />
	</form>

	<%
		List<Document> DocList = (List<Document>) request.getAttribute("DocList");
		
		if (DocList != null) {	
	%>
	<br/>
	<table border="1">
		<tr>
			<td>Feature Name</td>
			<td>Feature Class</td>
			<td>State Alpha</td>
			<td>County Name</td>
			<td>Longitude</td>
			<td>Latitude</td>
			<td>Elevation (m))</td>
			<td>Elevation (ft)</td>
			<td>Map Name</td>
			<td>Description</td>
			<td>History</td>
			<td>Date Created</td>
			<td>Date Edited</td>
		</tr>
		<%
			for (int i = 0; i < DocList.size(); i++) {
		%>
		<tr>
			<td><%=DocList.get(i).getFeatureName()%></td>
			<td><%=DocList.get(i).getFeatureClass()%></td>
			<td><%=DocList.get(i).getStateAlpha()%></td>
			<td><%=DocList.get(i).getCountyName()%></td>
			<td><%=DocList.get(i).getLatitude()%></td>
			<td><%=DocList.get(i).getLongitude()%></td>
			<td><%=DocList.get(i).getElevationMeter()%></td>
			<td><%=DocList.get(i).getElevationFt()%></td>
			<td><%=DocList.get(i).getMapName()%></td>
			<td><%=DocList.get(i).getDescription()%></td>
			<td><%=DocList.get(i).getHistory()%></td>
			<td><%=DocList.get(i).getDateCreated()%></td>
			<td><%=DocList.get(i).getDateEdited()%></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}
	%>
</body>

</html>