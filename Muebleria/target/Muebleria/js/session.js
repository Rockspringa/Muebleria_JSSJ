const REGEX = new RegExp("^[\\w\\d]+$");
function isAlfanumeric(tag) {
    return REGEX.test($(tag).val());
}

$(document).ready(() => {
    $("#login").submit(() => {
        if (isAlfanumeric("#user") && isAlfanumeric("#user"))
            $.ajax({
                url: "../user_area",
                type: 'POST',
                data: { user: $("#user").val(), pass: $("#pass").val() },
                success: data => { data.includes('/') ? window.location.href = data : alert(data); }
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
