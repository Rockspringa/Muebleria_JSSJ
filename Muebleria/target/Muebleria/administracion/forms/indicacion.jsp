<div class="form-group">
    <label for="tipo<%= i %>">Tipo de la Pieza</label>
    <select class="form-control" id="tipo<%= i %>" name="tipo<%= i %>" aria-describedby="thelp" required>
        <option disabled selected>Tipo de la pieza</option>
    </select>
    <small id="thelp" class="form-text text-muted">Nombre de la pieza.</small>
</div>
<div class="form-group">
    <label for="costo">Costo de la Pieza</label>
    <div class="input-group mb-2">
        <div class="input-group-prepend">
            <div class="input-group-text">Q.</div>
        </div>
        <select class="form-control" id="costo<%= i %>" name="costo<%= i %>" required>
            <option disabled selected>Costo de la pieza</option>
        </select>
    </div>
</div>
<% i++; %>