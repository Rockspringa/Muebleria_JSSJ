<!DOCTYPE html>
<%! String title = "Modificacion en Proceso"; %>
<%! String cssLink = "../css/fabrica/cuad.css"; %>
<%! String jsLink = "../js/fabrica/cuad.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="repeated/topHtml.jsp" %>

    <section id="btns">
        <button type="button" class="cre press btn btn-secondary">Crear un Nuevo Tipo de Pieza</button>
        <button type="button" class="upd press btn btn-secondary">Modificar un Tipo Existente de Pieza</button>
        <button type="button" class="add press btn btn-secondary">Agregar Existencias a una Pieza</button>
        <button type="button" class="del press btn btn-secondary">Eliminar un Tipo Existente de Pieza</button>
    </section>
    
    <section id="box">
        <!-- Aqui va el form a llenar -->
    </section>
</body>

</html>