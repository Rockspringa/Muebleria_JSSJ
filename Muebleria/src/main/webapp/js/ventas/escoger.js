function llenarTiposMueble() {
    $.ajax({
        url: "/ventas/venta",
        type: "POST",
        data: {
            opcion: 'tipos'
        },
        success: data => {
            if (!data.includes('.')) {
                $('#forms').empty().load('/ventas/html/mueble.html', () => $('#tipo').append(data));
            } else {
                alert(data);
            }
        }
    });

}

var cantidad = 0;
var total = 0.0;

$(document).ready(() => {
    $.ajax({
        url: "/ventas/venta",
        type: "POST",
        data: {
            opcion: 'reset'
        },
        success: data => llenarTiposMueble()
    });

    $('#box').on('submit', 'form', function () {
        $.ajax({
            url: "/ventas/venta",
            type: 'POST',
            data: {
                opcion: 'agregar',
                id: $('#id').val(),
                tipo: $('#tipo').val()
            },
            success: data => {
                const tipo = $('#tipo').val();
                const precio = parseFloat($('#precio').val());
                const id = $('#id').val();
                total += precio;

                $.ajax({
                    type: "GET",
                    url: "/ventas/html/cont.html",
                    success: text => {
                        $('#carrito').append(text);
                        $('.empty .mueble').text("Mueble: " + tipo);
                        $('.empty .precio').text("Precio: Q." + precio);
                        $('.empty .id').text("Id: " + id);
                        $('#carrito .empty').removeClass('empty');
                    }
                });

                llenarTiposMueble();
                $('#box a').css('display', 'block');
                $('#cant').text(++cantidad);
                $('#total').text(total);
            }
        });
        return false;
    });

    $('#box').on('click', '#caja', function () {
        $.ajax({
            url: "/ventas/venta",
            type: 'POST',
            data: {
                opcion: 'agregar',
                id: $('#id').val(),
                tipo: $('#tipo').val()
            },
            success: data => {
                window.location.replace('/ventas/caja.jsp');
            }
        });
        return false;
    });

    $('#box').on('change', '#tipo', function () {
        $.ajax({
            url: "/ventas/venta",
            type: 'POST',
            data: {
                opcion: 'datos',
                tipo: $('#tipo').val()
            },
            success: data => {
                if (data.includes('_')) {
                    var datos = data.split('_');
                    $('#precio').val(datos[0]);
                    $('#id .del').remove();
                    $('#id').append(datos[1]);
                } else {
                    alert(data);
                }
            }
        });
        return false;
    });
});