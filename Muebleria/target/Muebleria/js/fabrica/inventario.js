$(document).ready(function() {
    $('#piezas-btn').click(function() {
        $('#creado-btn').removeClass('btn-primary').addClass('btn-secondary');
        $(this).removeClass('btn-secondary').addClass('btn-primary');

        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'piezasTab' },
            success: data => { data.includes('table') ? $('#box').html(data) : alert(data); }
        });
    });
    
    $('#creado-btn').click(function() {
        $('#piezas-btn').removeClass('btn-primary').addClass('btn-secondary');
        $(this).removeClass('btn-secondary').addClass('btn-primary');

        $.ajax({
            url: "/fabrica/queryDB",
            type: 'POST',
            data: { opcion: 'creadoTab' },
            success: data => { data.includes('table') ? $('#box').html(data) : alert(data); }
        });
    })
});