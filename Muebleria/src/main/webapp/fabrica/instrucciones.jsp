<!DOCTYPE html>
<%! String title = "Instruciones de un Mueble"; %>
<%! String cssLink = "../css/instr.css"; %>
<%! String jsLink = "../js/instruc.js"; %>
<%@include file="repeated/topHtml.jsp" %>

<body>
    <%@include file="repeated/header.jsp" %>
    
    <div class="alert alert-warning alert-dismissible">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>Info: </strong>Presione sobre un mueble para visualizar sus instrucciones.
        <br><strong>Info: </strong>Una columna de color rojo significa que no hay suficientes piezas para ensamblar el mueble seleccionado.
        <br><strong>Info: </strong>Una columna de color amarillo significa que hay muy pocas piezas.
        <br><strong>Info: </strong>Una columna de color verde significa que hay una buena cantidad de piezas.
    </div>

    <section class="cont">
        <aside id="muebles">
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