$(document).ready(function () {
    let form = $('form');
    form.submit(function () {
        form.dimmer({closable: false, variation: 'inverted', duration: {show: 50}}).dimmer('show');
        form.append('<div class="ui text active loader">Processing...</div>');
    });
});