const ALFANUMERIC = new RegExp("^[\\w ]+$");
const NUMS = new RegExp("^[\\d]+([.][\\d]+)?$");

function loadForm(tagClick, url, tagDestino = '#box') {
    $(tagClick).click(() => {
        $(tagDestino).load(url);
    });
}

function isAlfanumeric(tag) {
    return ALFANUMERIC.test($(tag).val());
}

function isNumsOnly(txt) {
    return NUMS.test(txt);
}

let normalColor = null;

$(document).ready(() => {
    $(".container").slideToggle("slow");

    $('.press').click(function() {
        $('.press').removeClass('btn-primary').addClass('btn-secondary');
        $(this).removeClass('btn-secondary').addClass('btn-primary');
    });

    $('form, #box').on('blur', '.limit-40', function() {
        if ($(this).val().length > 40) {
            alert('Ingrese un dato que tenga menos de 40 caracteres.');
            $(this).val('');
        };
    });

    $('form, #box').on('blur', '.limit-num', function() {
        if (! isNumsOnly($(this).val())) {
            alert('Ingrese un dato que tenga solamente numeros.');
            $(this).val('');
        }
    });

    $('form, #box').on('blur', ".in", function() {
        if (! isAlfanumeric(this)) $(this).css('background-color', 'lightcoral');
    });

    $('form, #box').on('focus', '.in', function() {
        if (normalColor == null) normalColor = $(this).css('background-color');
        $(this).css('background-color', normalColor);
    });
});
