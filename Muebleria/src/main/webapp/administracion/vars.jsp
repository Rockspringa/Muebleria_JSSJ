<%@ page import="edu.general.BotonHeader" %>

<%! String clase = "menu-item"; %>
<%! String area = "Area de Administracion"; %>
<%! BotonHeader[] btns = new BotonHeader[4]; %>

<%
    btns[0] = new BotonHeader("Carga de Datos", "import.jsp", clase);
    btns[1] = new BotonHeader("Reportes", "reportes.jsp", clase);
    btns[2] = new BotonHeader("Manejar Usuarios", "usuarios.jsp", clase);
    btns[3] = new BotonHeader("Crear Mueble", "mueble.jsp", clase);
    btns[index].addClass("selected");
%>