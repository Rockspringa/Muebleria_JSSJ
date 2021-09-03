<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="edu.general.UserArea" %>

<% UserArea.redirectToArea(request, response, session, area); %>

<html lang="es">

<head>
    <title><%= btns[index].getNombre() %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <script src="../js/jquery-3.6.0.min.js"></script>
    <script src="../js/general.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="<%= jsLink %>"></script>
    <link rel="stylesheet" type="text/css" href="../css/general.css">
    <link rel="stylesheet" type="text/css" href="<%= cssLink %>">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>

<body>
    <header class="menu">
        <a href="" class="home"><%= area %></a>
        <c:forEach items="<%= btns %>" var="btn">
            <a href="${ btn.getLink() }" class="${ btn.getClasses() }">${ btn.getNombre() }</a>
        </c:forEach>
    </header>
