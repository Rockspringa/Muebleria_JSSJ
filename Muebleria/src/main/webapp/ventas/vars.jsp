<%@ page import="edu.general.BotonHeader" %>

<%! String clase = "menu-item"; %>
<%! String area = "Area de Ventas"; %>
<%! BotonHeader[] btns = new BotonHeader[4]; %>

<%
    btns[0] = new BotonHeader("Escoger Muebles", "escoger.jsp", clase);
    btns[1] = new BotonHeader("Caja de Cobro", "caja.jsp", clase);
    btns[2] = new BotonHeader("Devolver un Mueble", "devolucion.jsp", clase);
    btns[3] = new BotonHeader("Consultas", "consultas.jsp", clase);
    btns[index].addClass("selected");
%>