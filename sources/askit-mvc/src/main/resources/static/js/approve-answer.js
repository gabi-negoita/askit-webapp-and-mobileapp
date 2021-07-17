$(document).ready(function () {
    // show reject modal
    $("#reject").click(function () {
        $('#reject-modal').modal({inverted: true}).modal('show');
    });

    // show approve modal
    $("#approve").click(function () {
        $('#approve-modal').modal({inverted: true}).modal('show');
    });
});