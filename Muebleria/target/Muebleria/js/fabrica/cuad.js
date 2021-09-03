function llenarSelectPiezas(data) {
    $('option.del').remove();
    $('#tipo').append(data);
}

function llenarSelectCostos(data) {
    $('#costo > .del').remove();
    $('#costo').append(data);
}

$(document).ready(() => {
    loadForm('button.cre', '../fabrica/forms/create.html');
    loadForm('button.upd', '../fabrica/forms/update.html');
    loadForm('button.add', '../fabrica/forms/add.html');
    loadForm('button.del', '../fabrica/forms/delete.html');

    $('#btns').on('click', 'button.upd, button.del, button.add', function() {
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'piezasOpt' },
            success: data => { data.includes('option') ? llenarSelectPiezas(data) : alert(data); }
        });
    });

    $('#box').on('change', 'select#tipo', function() {
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'costosOpt', pieza: $('#tipo').val() },
            success: data => { data.includes('option') ? llenarSelectCostos(data) : alert(data); }
        });
    });

    $('#box').on('change', 'select#costo', function() {
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'cantidOpt', pieza: $('#tipo').val(), costo: $('#costo').val() },
            success: data => { isNumsOnly(data) ? $('#cantidad').val(data) : alert(data); }
        });
    });

    $('#box').on('submit', 'form.add', function() {
        alert('Añadiendo existencias...');
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: {
                opcion: 'addPiezas',
                pieza: $('#tipo').val(),
                costo: $('#costo').val(),
                cantidad: $('#cantidad-n').val()
            },
            success: data => {
                alert(data);
                $('button.add').trigger('click');
            }
        });
        return false;
    });

    $('#box').on('submit', 'form.cre', function() {
        alert('Creando tipo de pieza...');
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: {
                opcion: 'crePiezas',
                pieza: $('#tipo').val(),
                costo: $('#costo').val(),
                cantidad: $('#cantidad').val()
            },
            success: data => {
                alert(data);
                $('button.cre').trigger('click');
            }
        });
        return false;
    });

    $('#box').on('submit', 'form.del', function() {
        alert('Eliminando tipo de pieza...');
        if (confirm('¿Esta seguro de eliminar el tipo "' + $('#tipo').val()
                    + '" con costo de Q.' + $('#costo').val() + '?')) {
            $.ajax({
                url: "/fabrica/queryDB",
                type: 'POST',
                data: {
                    opcion: 'delPiezas',
                    pieza: $('#tipo').val(),
                    costo: $('#costo').val(),
                    cantidad: $('#cantidad').val()
                },
                success: data => {
                    alert(data);
                    $('button.del').trigger('click');
                }
            });
        } else {
            alert('Eliminacion cancelada.');
        }
        return false;
    });

    $('#box').on('submit', 'form.upd', function() {
        const pieza_nueva = ($('#tipo-n').val().length > 0) ? $('#tipo-n').val() : $('#tipo').val();
        const costo_nuevo = ($('#costo-n').val().length > 0) ? $('#costo-n').val() : $('#costo').val();
        const canti_nueva = ($('#cantidad-n').val().length > 0) ? $('#cantidad-n').val()
                                                            : $('#cantidad').val();

        alert('Actualizando tipo de pieza...');
        if ($('#tipo').val() !== null && $('#costo').val() !== null
                    && confirm('¿Esta seguro de cambiar los datos del tipo "'
                    + $('#tipo').val() + '" con costo de Q.' + $('#costo').val() + '?')) {
            $.ajax({
                url: "/fabrica/queryDB",
                type: 'POST',
                data: {
                    opcion: 'updPiezas',
                    pieza: $('#tipo').val(),
                    costo: $('#costo').val(),
                    pieza_n: pieza_nueva,
                    costo_n: costo_nuevo,
                    cantidad_n: canti_nueva
                },
                success: data => {
                    alert(data);
                    $('button.upd').trigger('click');
                }
            });
        } else {
            alert('Actualizacion cancelada.');
        }
        return false;
    });
});