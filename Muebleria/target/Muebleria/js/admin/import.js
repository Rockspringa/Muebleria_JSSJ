$(document).ready(() => {
    $(".form-group > button").click(() => {
        $('#file').trigger('click');
        return false;
    })

    $("#file").change(function() {
        $('#filename').text('Nombre del archivo:\n' + $(this).val());
        $("#submit").prop('disabled', false).addClass("btn-primary").removeClass("btn-secondary");
    });

    $("form.import").submit(function() {
        $.ajax({
            url: "/administracion/upload",
            enctype: "multipart/form-data",
            type: 'POST',
            data: new FormData(this),
            processData: false,
            contentType: false,
            success: data => {
                data.includes('<li') ? $('#text-area').html(data) : alert(data);
            }
        });
        return false;
    });

    $("#submit").prop('disabled', true);
});