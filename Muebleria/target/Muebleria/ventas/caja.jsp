<!DOCTYPE html>
<%! int index = 1; %>
<%! String cssLink = "../css/ventas/caja.css"; %>
<%! String jsLink = "../js/ventas/caja.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="vars.jsp" %>
<%@include file="/top.jsp" %>

    <br>
    <div class="alert alert-warning alert-dismissible">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>Info: </strong>Caja
    </div>
    
    <section id="box">
        <h1 class="bg-primary text-white">Facturando</h1>
        <div id="factura">
            <div class="flex">
                <div class="flex">
                    <div>No. Factura</div>
                    <div id="id-factura">Aun no generado</div>
                </div>
                <div class="flex">
                    <div>Fecha:</div>
                    <div id="fecha"></div>
                </div>
            </div>
            <div class="flex">
                <div>Le atiende:</div>
                <div id="vendedor">${user}</div>
            </div>
            <div class="flex">
                <div class="flex">
                    <div>Nit:</div>
                    <div id="nit-s"></div>
                    <div>Nombre:</div>
                </div>
                <div id="cliente"></div>
            </div>
            <div class="flex">
                <div>Direccion:</div>
                <div id="direc-s"></div>
                <div>Municipio:</div>
                <div id="muni-s"></div>
                <div>Departamento:</div>
                <div id="depar-s"></div>
            </div>
            <div id="detalle">

            </div>
        </div>
        <div id="forms">
            <h1 class="bg-primary text-white">Informacion del Cliente</h1>
            <form method="POST">
                <div class="form-group">
                    <label for="nit">Nit del Cliente</label>
                    <input type="text" class="in form-control limit-7" id="nit" name="nit" placeholder="000000k" required>
                </div>
                <button type="submit" class="btn btn-primary">Agregar a la Factura</button>
            </form>
        </div>
        <button type="button" class="show">Crear Factura</button>
    </section>
</body>

</html>