const fecha = new Date();
let todosLosCampos = false;

$(document).ready(() => {
    $('#fecha').text(fecha.getDate() + "/" + (fecha.getMonth() + 1) + "/" + fecha.getFullYear());

    $.ajax({
        url: "/ventas/factura",
        type: "POST",
        data: { opcion: "muebles" },
        success: data => {
            if (data.includes('null'))
                $('#forms').hide();
            else
                $('#detalle').append(data);
        }
    });

    $('#nit').blur(() => {
        if ($('#nit').val().lenght == 7) {
            $.ajax({
                url: "/ventas/factura",
                type: 'POST',
                data: { opcion: 'cliente', nit: $('#nit').val() },
                success: data => {
                    if (data.includes('_')) {
                        todosLosCampos = false;
                        let nit = $('#nit').val();
                        $()
                        try {
                            let datos = data.split('_');
                            $('#nit-s').text(nit);
                            $('#cliente').text(datos[0]);
                            $('#direc-s').text(datos[1]);

                            if (!datos[1].includes('ciudad')) {
                                $('#muni-s').text(datos[2]);
                                $('#depar-s').text(datos[3]);
                            }
                            $('form > button').trigger('click');
                        } catch (e) { }
                    } else {
                        todosLosCampos = true;
                        $('#forms').load('/ventas/html/cliente.html', () => $('#nit').val(nit));
                        $('form > button').prop('disabled', false);
                    }
                }
            });
        }
    });

    $('#box').on('submit', '#forms', () => {
        if ($('#nit').val().lenght == 7 && !todosLosCampos) {
            $('#forms').remove();
            $('button.show').show();
            $('button.show').addClass('ready');
        } else {
            $.ajax({
                url: "/ventas/factura",
                type: 'POST',
                data: {
                    opcion: 'agregar',
                    nit: $('#nit').val(),
                    nombre: $('#nombre').val(),
                    direc: $('#direc').val(),
                    muni: $('#muni').val(),
                    depar: $('#depar').val()
                },
                success: data => {
                    if (data.includes('success')) {
                        $('#forms').remove();
                        $('button.show').show();
                        $('button.show').addClass('ready');
                    } else {
                        alert(data);
                    }
                }
            });
        }
        return false;
    });

    $('#box').on("click", "button.ready", () => {
        $.ajax({
            url: "/ventas/factura",
            type: "POST",
            data: { opcion: "factura" },
            success: data => {
                if (data.includes('null'))
                    $('#detalle').append(data);
                else
                    $('#forms').hide();
            }
        });
    });
});