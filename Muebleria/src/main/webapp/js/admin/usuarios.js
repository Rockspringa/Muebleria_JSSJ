function llenarSelectPiezas(data) {
    $('option.del').remove();
    $('#user').append(data);
}

$(document).ready(() => {
    loadForm('button.cre', '../administracion/forms/crear_usuario.html');
    loadForm('button.mod', '../administracion/forms/mod_usuario.html');
    loadForm('button.can', '../administracion/forms/cancelar_usuario.html');
    $("#btns").css('grid-template-columns', 'repeat(3, 1fr)');

    $("#box").on('blur', 'input#user', function() {
        $.ajax({
            url: "/administracion/usuarios",
            type: 'POST',
            data: {
                option: 'verificar',
                user: $('#user').val(),
            },
            success: data => {
                if (data.includes("exists")) {
                    $(this).val('');
                    $('#thelp')
                        .text('Debe de ser unico')
                        .removeClass('text-success text-muted')
                        .addClass('text-danger');
                    alert('El nombre de usuario ya existe.');
                } else if (data.includes("se")) {
                    $(this).val('');
                    alert(data);
                } else {
                    $('#thelp')
                        .text('Disponible')
                        .removeClass('text-muted text-danger')
                        .addClass('text-success');
                }
            }
        });
    });

    $("#box").on('change', 'select#user', function() {
        $.ajax({
            url: "/administracion/usuarios",
            type: 'POST',
            data: {
                option: 'datos',
                user: $('#user').val(),
            },
            success: data => {
                if (data.includes("_")) {
                    let datos = data.split("_");

                    if (datos[0] == "1")
                        $("#tipo_v").val("Area de fabricacion");
                    else if (datos[0] == "2")
                        $("#tipo_v").val("Area de ventas");
                    else if (datos[0] == "3")
                        $("#tipo_v").val("Area de administracion");
                    
                    if (datos[1] == "false")
                        $("#estado").val("El usuario ya esta cancelado.");
                    else if (datos[1] == "true")
                        $("#estado").val("El usuario esta activo.");
                }
            }
        });
    });

    $('#box').on('submit', 'form.cre', function() {
        let type = $('#tipo').val().substring(0, 1);
        if (type == 1 || type == 2 || type == 3)
            $.ajax({
                url: "/administracion/usuarios",
                type: 'POST',
                data: {
                    option: 'crear',
                    user: $('#user').val(),
                    pass: $('#pass').val(),
                    tipo: type
                },
                success: data => {
                    alert(data);
                    $('button.cre').trigger('click');
                }
            });
        return false;
    });

    $('#box').on('submit', 'form.mod', function() {
        let type = "";
        try {
            type = $('#tipo').val().substring(0, 1);
        } catch (e) { }

        $.ajax({
            url: "/administracion/usuarios",
            type: 'POST',
            data: {
                option: 'actualizar',
                user: $('#user').val(),
                pass: $('#pass').val(),
                tipo: type
            },
            success: data => {
                alert(data);
                $('button.mod').trigger('click');
            }
        });
        return false;
    });

    $('#box').on('submit', 'form.can', function() {
        $.ajax({
            url: "/administracion/usuarios",
            type: 'POST',
            data: {
                option: 'cancelar',
                user: $('#user').val()
            },
            success: data => {
                alert(data);
                $('button.can').trigger('click');
            }
        });
        return false;
    });

    $('#btns').on('click', 'button.mod', function() {
        $.ajax({
            url: "/administracion/usuarios",
            type: 'POST',
            data: { option: 'usuarios' },
            success: data => { data.includes('option') ? llenarSelectPiezas(data) : alert(data); }
        });
    });

    $('#btns').on('click', 'button.can', function() {
        $.ajax({
            url: "/administracion/usuarios",
            type: 'POST',
            data: { option: 'activos' },
            success: data => { data.includes('option') ? llenarSelectPiezas(data) : alert(data); }
        });
    });
});