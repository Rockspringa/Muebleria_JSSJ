$(document).ready(() => {
    $('#report-afk-troll').submit(() => {
        let inicio_ = "28/09/1980";
        let final_ = "28/09/2060";

        if ($('#inicio').val() != "") inicio_ = $('#inicio').val();
        if ($('#final').val() != "") final_ = $('#final').val();

        $.ajax({
            url: "/administracion/reportes",
            type: 'POST',
            data: {
                opcion: $('#reporte').val(),
                inicio: inicio_,
                final: final_
            },
            success: data => {
                if (data.includes('<table')) {
                    $('#tabla').html(data);

                    $.ajax({
                        url: "/administracion/download",
                        type: 'POST',
                        data: {
                            opcion: $('#reporte').val(),
                            inicio: inicio_,
                            final: final_
                        },
                        success: data => ( $('#virus-download').show() )
                    });
                } else {
                    alert(data);
                }
            }
        });
        return false;
    });
});