<!DOCTYPE html>
<%! int index = 2; %>
<%! String cssLink = "../css/fabrica/cuad.css"; %>
<%! String jsLink = "../js/admin/usuarios.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="vars.jsp" %>
<%@include file="/top.jsp" %>

    <section id="btns">
        <button type="button" class="cre press btn btn-secondary">Crear un Usuario</button>
        <button type="button" class="mod press btn btn-secondary">Modificar un Usuario</button>
        <button type="button" class="can press btn btn-secondary">Cancelar un Usuario</button>
    </section>
    
    <section id="box">
        <!-- Aqui va el form a llenar -->
    </section>
</body>

</html>