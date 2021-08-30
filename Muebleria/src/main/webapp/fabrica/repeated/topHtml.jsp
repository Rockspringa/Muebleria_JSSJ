<%@ page import="edu.general.UserArea" %>
<% UserArea.redirectToArea(request, response, session, "fabrica"); %>

<html lang="es">

<head>
    <title><%= title %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <script src="../js/jquery-3.6.0.min.js"></script>
    <script src="../js/animations.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%= cssLink %>">
    <link rel="stylesheet" type="text/css" href="../css/general.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
