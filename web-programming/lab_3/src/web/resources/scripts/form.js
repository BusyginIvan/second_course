jQuery.noConflict();
let currentR;

jQuery(document).ready(function($) {
    $('.textX').val(0);
    $('.ui-spinner-input').val(3);
    console.log('ready 1: ' + currentR);
    setR();
    console.log('ready 2: ' + currentR);

    function initListeners(element, min, max, action) {
        if (action === undefined) action = () => {};
        function validateText() {
            let x = textToNum(element);
            if (x > max) element.val(max);
            else if (x < min) element.val(min);
        }
        element.on('focusout', () => { validateText(); action() });
        element.bind('keydown', (event) => {
            if (event.which === 13) { validateText(); action() }
        });
    }

    initListeners($('.textX'), -5, 5);
    initListeners($('.textY'), -5, 5);
    initListeners($('.ui-spinner-input'), 1, 3, setR);
    $('.spinnerR > .ui-spinner-button').on('click', setR);

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