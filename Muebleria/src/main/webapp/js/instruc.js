$(document).ready(function() {
    
    $.ajax({
        url: "/fabrica/muebles",
        type: 'POST',
        data: { opcion: 'mueblesLi' },
        success: data => { data.includes('li') ? $('#muebles > ul').html(data) : alert(data); }
    });

    $('ul').on("click", "li", function() {
        let n_mueble = $(this).text();
        $('#titulo').text(n_mueble);

        $.ajax({
            url: "/fabrica/muebles",
            type: 'POST',
            data: { opcion: 'precios', mueble: n_mueble },
            success: data => {
                if (data.includes("_")) {
                    let precios = data.split("_");
                    $('#precio').text(precios[0]);
                    $('#coste').text(precios[1]);
                }
                else alert(data);
            }
        });

        $.ajax({
            url: "/fabrica/muebles",
            type: 'POST',
            data: { opcion: 'indicacio', mueble: n_mueble },
            success: data => { data.includes('table') ? $('#content').html(data) : alert(data); }
        });
    });
})