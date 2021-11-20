jQuery.noConflict();

jQuery(document).ready(function($) {
    $('.textX').val(0);
    $('.ui-spinner-input').val(currentR);

    function validateText(element, min, max) {
        let x = textToNum(element);
        if (x > max) element.val(max);
        else if (x < min) element.val(min);
    }

    function initListeners(element, action) {
        element.on('focusout', action);
        element.bind('keydown', function(event) {
            if (event.which === 13) action();
        });
    }

    initListeners($('.ui-spinner-input'), () => { validateText($('.ui-spinner-input'), 1, 3); setR() });

    $('.spinnerR > .ui-spinner-button').on('click', setR);

    initListeners($('.textX'), () => validateText($('.textX'), -5, 5));

    /*$('.button').on('click', redraw);*/

    $('#canvas').mousedown(function (event) {
        let norm = k => Math.round((k - 2 - arrowsLength - indent) / scale * divisionsNumber * 1000) / 1000;
        $('.textX').val(norm(event.pageX - $(this).offset().left));
        $('.textY').val(-norm(event.pageY - $(this).offset().top));
        $('.submit').click();
    });
});

function setR() {
    currentR = textToNum(jQuery('.ui-spinner-input'));
    redraw();
}

function textToNum(element) { return Number(element.val().replace(',', '.')) }