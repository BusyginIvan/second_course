"use strict";

let x = 1, y = 0, r = 2;

function initForm() {
    $('#textY').on('input', function() { $(this).css('color', 'black') });

    $('input[name=checkboxR]').change(function() {
        if ($(this).is(':checked')) {
            $('input[name=checkboxR]').prop('checked', false);
            $(this).prop('checked', true);
        }
    });

    $('#checkButton').click(() => {
        r = $('input[name=checkboxR]:checked').val();
        x = $('input[name=radioX]:checked').val();
        y = $('#textY').val().replace(',', '.');

        if (validateY()) newPoint();
    });

    $('#canvas').mousedown(function (event) {
        r = $('input[name=checkboxR]:checked').val();
        x = normalize(event.pageX - $(this).offset().left - 2);
        y = -normalize(event.pageY - $(this).offset().top - 2);

        newPoint();
    });
}

function normalize(x) {
    return (x - indent - semiaxisLength) / R * r;
}

function newPoint() {
    setPointer(x, y, r);
    $.get({
        url: 'server',
        data: 'x=' + encodeURIComponent(x) +
              '&y=' + encodeURIComponent(y) +
              '&r=' + encodeURIComponent(r),
        dataType: 'html',
        headers: { 'X-Requested-With': 'XMLHttpRequest',
                   'Accept-Charset': 'UTF-8'   },
        success: data => $('#outputContainer').html(data)
    });
    /*err => alert(err.status + " – ошибка HTTP.\n" + err.message)*/
}

function validateY() {
    if (y === undefined || !isNumeric(y) || (y < -5) || (y > 5)) {
        $("#textY").css('color', 'red');
        return false;
    } return true;
}

function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}