<!DOCTYPE html>
<%! String title = "Construir Mueble"; %>
<%! String cssLink = "../css/construir.css"; %>
<%@include file="repeated/topHtml.jsp" %>

<body>
    <%@include file="repeated/header.jsp" %>
    <div class="alert alert-warning alert-dismissible">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>Info: </strong>Presione sobre el mueble que quiere construir,
            se agregara automaticamente a la seccion de ventas.
    </div>
    </section>
    <section id="muebles">
        <div><b>Piezas necesarias</b>
            <ul>
            </ul>
        </div>
        <a><img src="/resources/images/mueble.png" alt="mueble"><br />mueble 1</a>
    </section>
</body>

</html>