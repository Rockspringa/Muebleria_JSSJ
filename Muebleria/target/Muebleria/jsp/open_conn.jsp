<%@ page import="edu.persis.CallQuery" %>

<%
    System.out.println(CallQuery.createSession());
    out.println(CallQuery.isUsuario(request.getParameter("user"), request.getParameter("pass")));
%>
