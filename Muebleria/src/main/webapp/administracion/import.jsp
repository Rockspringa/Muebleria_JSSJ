<!DOCTYPE html>
<%! int index = 0; %>
<%! String cssLink = "../css/admin/import.css"; %>
<%! String jsLink = "../js/admin/import.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="vars.jsp" %>
<%@include file="/top.jsp" %>

    <br>
    <div class="alert alert-warning alert-dismissible">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>Info: </strong>Ingrese el archivo que contiene la informacion de carga,
        a continuacion (el archivo debe de ser utf-8):
    </div>
    <section id="box">
        <aside>
            <form class="import" method="POST" enctype="multipart/form-data" action="/administracion/upload">
                <div class="form-group">
                    <label for="upload">Archivo de datos: </label>
                    <button class="btn btn-primary">Elegir Archivo</button>
                    <input type="file" class="form-control-file" name="file" id="file">
                    <div class="text-muted" id="filename"></div>
                    <input type="submit" class="btn btn-secondary" value="Cargar Archivo" id="submit">
                </div>
            </form>
        </aside>
        <ol id="text-area" class="list-group list-group-numbered border border-primary">
        </ol>
    </section>
</body>

</html>