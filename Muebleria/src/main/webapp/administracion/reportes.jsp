<!DOCTYPE html>
<%! int index = 1; %>
<%! String cssLink = "../css/admin/reportes.css"; %>
<%! String jsLink = "../js/admin/reportes.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="vars.jsp" %>
<%@include file="/top.jsp" %>

    <br>
    <section id="box">
        <div class="alert alert-warning alert-dismissible">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong>Info: </strong>Escoja el reporte 
        </div>
        <form id="report-afk-troll" method="POST">
            <div class="form-group">
                <label for="reporte">Escoja un reporte:</label>
                <select class="form-control" id="reporte" name="reporte" required>
                    <option disabled selected>Elija el reporte que desea ver</option>
                    <option>Reporte de ventas</option>
                    <option>Reporte de devoluciones</option>
                    <option>Reporte de ganancias</option>
                    <option>Reporte del vendedor con mas ventas</option>
                    <option>Reporte del vendedor con mas ganancias</option>
                    <option>Reporte del mueble mas vendido</option>
                    <option>Reporte del mueble menos vendido</option>
                </select>
            </div>
            <div class="form-group dates">
                <div class="form-group">
                    <label for="inicio">desde:</label>
                    <input type="date" class="form-control" id="inicio" name="inicio">
                </div>
                <div class="form-group">
                    <label for="final">hasta:</label>
                    <input type="date" class="form-control" id="final" name="final">
                </div>
                <input type="submit" class="btn btn-primary" id="submit" value="Ver">
            </div>
        </form>
        <div id="cont">
            <div id="tabla">
                <!-- Aqui se agragara el reporte dinamicamente -->
            </div>
            <a id="virus-download" href="/administracion/download?virus=true" class="btn btn-primary">Exportar como DotCSV</a>
        </div>
    </section>
</body>

</html>