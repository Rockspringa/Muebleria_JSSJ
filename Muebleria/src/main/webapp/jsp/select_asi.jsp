<%@ page import="edu.persis.CallQuery" %>

<% out.print(CallQuery.selectAsignacion("estudiante", ">", request.getParameter("carnet"))); %>
