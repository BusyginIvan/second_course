"use strict";

function initForm() {
    $('#textY').on('input', function () { $(this).css('color', 'black') });

    $('#checkButton').click(() => {
        let x = $('input[name=radioX]:checked').val();
        let y = parseY($('#textY').val().replace(',', '.'));
        if (!isNaN(y)) for_each_r((a) => a, x, y);
    });

    $('#canvas').mousedown(function (event) {
        let x = normalize(event.pageX - $(this).offset().left);
        let y = -normalize(event.pageY - $(this).offset().top);
        for_each_r((a, r) => a * r, x, y);
    });
}

function for_each_r(action, x, y) {
    $('input[name=checkboxR]:checked').each(function () {
        let r = $(this).val();
        newPoint(action(x, r), action(y, r), r);
    });
}

function normalize(x) {
    return (x - 2 - indent - semiaxisLength) / R;
}

function newPoint(x, y, r) {
    drawPointer(x, y, r);
    $.get({
        url: 'server',
        data: 'x=' + x +
              '&y=' + y +
              '&r=' + r,
        dataType: 'html',
        headers: { 'X-Requested-With': 'XMLHttpRequest' },
        success: data => $('#outputContainer').html(data)
    });
}

function parseY(y) {
    if (y === undefined || isNaN(y) || (y < -5) || (y > 5)) {
        $("#textY").css('color', 'red');
        return NaN;
    }
    return parseFloat(y);
}