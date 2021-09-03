<%@ page import="edu.general.BotonHeader" %>

<%! String clase = "menu-item"; %>
<%! String area = "Area de Fabrica"; %>
<%! BotonHeader[] btns = new BotonHeader[4]; %>

<%
    btns[0] = new BotonHeader("Construir un Mueble", "construir.jsp", clase);
    btns[1] = new BotonHeader("Indicaciones", "instrucciones.jsp", clase);
    btns[2] = new BotonHeader("Inventarios", "inventario.jsp", clase);
    btns[3] = new BotonHeader("Modificar Inventario", "modificar.jsp", clase);
    btns[index].addClass("selected");
%>