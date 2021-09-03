
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
});
