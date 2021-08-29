const REGEX = new RegExp("^[\\w\\d]+$");
function isAlfanumeric(tag) {
    return REGEX.test($(tag).val());
}

$(document).ready(() => {
    $(".in").blur(() => {
        if (! isAlfanumeric(this))
            $(this).css({ backgroundColor: 'lightcoral', borderBottomColor: 'red' });
    });

    $('.in').focus(() => {
        $(this).css({ backgrounColor: 'lightblue', borderBottomColor: '#142341' });
    });

    $("#login").submit(() => {
        if (isAlfanumeric("#user") && isAlfanumeric("#user"))
            $.ajax({
                url: "jsp/open_conn.jsp",
                type: 'POST',
                data: { user: $("#user").val(), pass: $("#pass").val() },
                success: data => {
                        if (data.length > 1) {
                            if (data.includes("3"))
                                window.location.href = "fabrica/construir.jsp";
                        } else {
                            alert("No existe el registro");
                        }
                    }
                });
        else alert("Por favor, ingrese solo caracteres alfanumericos");
        return false;
    });
    /*$.ajax({
        url: "jsp/open_conn.jsp",
        success: data => {
            if (data.includes("ready") || data.includes("existente")) {
                ajaxCall("jsp/select_asi.jsp", '#asignacion', { carnet: 0 },
                    ajaxCall("jsp/select_cur.jsp", '#curso', { codigo: 0 },
                        ajaxCall("jsp/select_est.jsp", '#estudiante', { carnet: 0 },
                            () => {
                                $.ajax({
                                    url: "jsp/close_conn.jsp",
                                    type: 'post',
                                    async: false,
                                    timeout: 5000
                                })
                            }
                        )
                    )
                );
            } else alert(data);
        }
    })*/
});
