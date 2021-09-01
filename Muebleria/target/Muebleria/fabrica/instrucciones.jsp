<!DOCTYPE html>
<%! String title = "Instruciones de un Mueble"; %>
<%! String cssLink = "../css/fabrica/instr.css"; %>
<%! String jsLink = "../js/fabrica/instruc.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="repeated/topHtml.jsp" %>

    <div class="alert alert-warning alert-dismissible">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>Info: </strong>Presione sobre un mueble para visualizar sus instrucciones.
        <br><strong>Info: </strong>Una columna de color rojo significa que no hay suficientes piezas para ensamblar el mueble seleccionado.
        <br><strong>Info: </strong>Una columna de color amarillo significa que hay muy pocas piezas.
        <br><strong>Info: </strong>Una columna de color verde significa que hay una buena cantidad de piezas.
        <br><strong>Info: </strong>Dele unos segundos antes de consultar los datos, el encargado de traer los datos no corre mucho.
    </div>

    <section class="cont">
        <aside id="muebles">
            <h1>Muebles</h1>
            <ul class="list-group list-group-flush">
                <!-- Aqui van los tipos de muebles -->
            </ul>
        </aside>
        <div id="instruc">
            <h1 id="titulo"><!-- Aqui va el nombre del mueble --></h1>
            <span id="precio" class="badge badge-primary"><!-- Aqui va el precio del mueble --></span>
            <span id="coste" class="badge badge-primary"><!-- Aqui va el nombre del mueble --></span>

            <div id="content">
                <!-- Aqui va una tabla con las piezas que lleva el mueble -->
            </div>
        </div>
    </section>
</body>

</html>