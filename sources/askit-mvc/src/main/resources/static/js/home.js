$(document).ready(function () {
    let form = $('form');

    form.off('submit');
    form.submit(
        function () {
        $('.ui.search').addClass('loading');
    });
});