<%@ page import="edu.general.UserArea" %>
<% UserArea.redirectToArea(request, response, session, "fabrica"); %>

<html lang="es">

<head>
    <title><%= title %></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <script src="../js/jquery-3.6.0.min.js"></script>
    <script src="../js/general.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="<%= jsLink %>"></script>
    <link rel="stylesheet" type="text/css" href="<%= cssLink %>">
    <link rel="stylesheet" type="text/css" href="../css/general.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>

<body>
    <header class="menu">
        <a href="" class="home">Area de Fabrica</a>
        <a href="construir.jsp" class="menu-item <%= (title.contains("Construir")) ? "selected" : "" %>" id="con-mue">Construir un Mueble</a>
        <a href="instrucciones.jsp" class="menu-item <%= (title.contains("Instruc")) ? "selected" : "" %>" id="lib-ind">Indicaciones</a>
        <a href="inventario.jsp" class="menu-item <%= (title.contains("Invent")) ? "selected" : "" %>" id="inv-pie">Inventarios</a>
        <a href="crud.jsp" class="menu-item <%= (title.contains("Creados")) ? "selected" : "" %>" id="mue-cre">Modificar Inventario</a>
    </header>
