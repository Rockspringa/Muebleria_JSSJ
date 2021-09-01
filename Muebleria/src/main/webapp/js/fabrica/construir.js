var muebleActual = null;
function llamarMuebles() {
    $.ajax({
        url: "/fabrica/queryDB",
        type: 'POST',
        data: { opcion: 'muebles' },
        success: data => { data.includes('button') ? $('#muebles').append(data) : alert(data); }
    });
}

$(document).ready(() => {
    llamarMuebles();

    $('#create').prop('disabled', true);
    
    $('section').on('click', 'button.btn', function() {
        $('.create').removeClass('btn-secondary btn-danger btn-success');
        $('#create').prop('disabled', true);
        muebleActual = $(this).text();
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'piezas', mueble: muebleActual },
            success: data => {
                data.includes('li') ? $('ul').html(data) : alert(data);
                data.includes('danger') ? $('#create').addClass('btn-danger')
                                        : $('#create').addClass('btn-success').prop('disabled', false);
            }
        });
        return false;
    });
    
    $('#create').click(() => {
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'ensamblar', mueble: muebleActual },
            success: data => {
                let title = "";
                let body = "";
                let clase = "";

                if (data.includes('success')) {
                    title = 'Se ensamblo con exito';
                    body = 'El mueble "' + muebleActual + '" se ensamblo con exito.';
                    clase = "btn-success";
                } else if (data.includes('mueble')) {
                    title = 'El mueble no existe';
                    body = 'Buscamos el mueble "' + muebleActual + '" pero no encontramos informacion de el.';
                    clase = "btn-danger";
                } else if (data.includes('usuario')) {
                    title = 'El usuario no existe';
                    body = 'Buscamos el usuario actual, pero no encontramos informacion de el.';
                    clase = "btn-danger";
                } else if (data.includes('piezas')) {
                    title = 'No se puede ensamblar el mueble';
                    body = 'No hay las suficientes piezas para poder armar un "' + muebleActual + '".';
                    clase = "btn-warning";
                }

                $('.modal-title').text(title);
                $('.modal-body').text(body);
                $('.modal-footer > button')
                    .removeClass('btn-secondary btn-danger btn-success btn-warning')
                    .addClass(clase);
            }
        });
    });

    $('#close-modal').click(() => {
        $('ul').empty();
        $('#muebles > .btn').remove();
        llamarMuebles();
    })
});