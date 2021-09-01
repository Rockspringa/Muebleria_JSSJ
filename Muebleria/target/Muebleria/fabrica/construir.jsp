<!DOCTYPE html>
<%! String title = "Construir Mueble"; %>
<%! String cssLink = "../css/fabrica/construir.css"; %>
<%! String jsLink = "../js/fabrica/construir.js"; %>
<!-- Se incluye el header con el titulo y links -->
<%@include file="repeated/topHtml.jsp" %>

  <div class="alert alert-warning alert-dismissible">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <strong>Info: </strong>Presione sobre el mueble que quiere construir,
    se agregara automaticamente a la seccion de ventas.
  </div>
  </section>

  <section id="muebles">
    <div><b>Piezas necesarias</b>
      <ul class="list-group list-group-flush">
        <!-- Aqui iran las piezas del mueble -->
      </ul>
      <input id="create" type="button" class="btn btn-secondary" value="Crear mueble" data-toggle="modal" data-target="#popup">
    </div>
    <!-- Aqui iran los cuadros de muebles para crear -->

  </section>

  <!-- Mensaje emergente sobre si se construyo el mueble -->
  <div class="modal" id="popup">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title"><!-- Ingreso de texto por jquery --></h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>

        <div class="modal-body">
          <!-- Ingreso de texto por jquery -->
        </div>

        <div class="modal-footer">
          <button id="close-modal" type="button" class="btn" data-dismiss="modal">Cerrar</button>
        </div>

      </div>
    </div>
  </div>
</body>

</html>