<!DOCTYPE html>
<%! int index = 0; %>
<%! String cssLink = "../css/ventas/escoger.css"; %>
<%! String jsLink = "../js/ventas/escoger.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="vars.jsp" %>
<%@include file="/top.jsp" %>

    <br>
    <div class="alert alert-warning alert-dismissible">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>Info: </strong>Puede escoger los muebles que mas le gusten:
    </div>
    
    <section id="box">
        <h1 class="bg-primary text-white">Escoger Muebles</h1>
        <div id="carrito">
        </div>
        <div id="forms">
            <!-- Aqui va el form a llenar -->
        </div>
        <div id="totales" class="alert alert-info">
            <div><h4><strong>Cantidad de muebles:</strong>  </h4><h4 id="cant">0</h4></div>
            <div><h4><strong>Total:</strong> Q.</h4><h4 id="total">0.00</h4></div>
        </div>
        <a href="/ventas/caja.jsp" class="btn btn-secondary">Ir a caja</a>
    </section>
</body>

</html>