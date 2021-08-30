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
            <ul class="list-group list-group-flush">
            </ul>
            <input id="create" type="button" class="btn btn-secondary" value="Crear mueble">
        </div>
    </section>
</body>

</html>