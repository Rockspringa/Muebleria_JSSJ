<!DOCTYPE html>
<%! String title = "Inventarios"; %>
<%! String cssLink = "../css/fabrica/invent.css"; %>
<%! String jsLink = "../js/fabrica/inventario.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="repeated/topHtml.jsp" %>

    <div class="alert alert-warning alert-dismissible">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>Info: </strong>Presione el boton correspondiente para ver un inventario.
        Si hay una tabla vacia, es que no hay ninguna pieza agragada o mueble creado.
    </div>

    <section id="btns">
        <button id="piezas-btn" type="button" class="press btn btn-secondary">Inventario de Piezas</button>
        <button id="creado-btn" type="button" class="press btn btn-secondary">Inventario de Mesas Ensambladas</button>
    </section>

    <section id="box">
        <!-- Aqui va la tabla de datos -->
    </section>
</body>

</html>