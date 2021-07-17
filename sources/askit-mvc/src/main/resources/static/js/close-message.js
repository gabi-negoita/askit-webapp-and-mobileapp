$(document).ready(function(){
    $('.ui.message i').on('click', function () {
        $('#message').closest('.message').transition('scale');
    });
});