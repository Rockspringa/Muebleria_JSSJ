$(document).ready(() => {
    $.ajax({
        url: "/fabrica/muebles",
        type: 'POST',
        data: { datos: 'muebles' },
        success: data => { data.includes('button') ? $('#muebles').append(data) : alert(data); }
    });
    
    $('section').on('click', 'button.btn', function() {
        $('.create').removeClass('btn-secondary');
        $.ajax({
            url: "/fabrica/muebles",
            type: 'POST',
            data: { datos: 'piezas', mueble: $(this).text() },
            success: data => {
                data.includes('li') ? $('ul').html(data) : alert(data);
                data.includes('danger') ? $('.create').addClass('btn-danger') : $('btn-success');
            }
        });
        return false;
    });
})