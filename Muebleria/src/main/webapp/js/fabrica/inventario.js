$(document).ready(function() {
    $('#piezas-btn').click(function() {
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'piezasTab' },
            success: data => { data.includes('table') ? $('#box').html(data) : alert(data); }
        });
    });
    
    $('#creado-btn').click(function() {
        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'creadoTab' },
            success: data => { data.includes('table') ? $('#box').html(data) : alert(data); }
        });
    })
});