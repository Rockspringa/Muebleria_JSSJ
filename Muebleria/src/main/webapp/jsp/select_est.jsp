<%@ page import="edu.persis.CallQuery" %>

<% out.print(CallQuery.selectEstudiante("no_carnet", ">", request.getParameter("carnet"))); %>
