$(document).ready(function(){
    tinymce.init({
        selector: '.mceReadonly',
        skin: 'custom-editor',
        readonly : true,
        menubar: false,
        toolbar: false,
        statusbar: false,
        branding: true,
        plugins: 'autoresize',
        autoresize_bottom_margin: 0
    });
});