$(document).ready(function () {
    tinymce.init({
        selector: '#body',
        skin: 'custom-editor',
        branding: false,
        nonbreaking_force_tab: true,
        menubar: 'file edit view insert format table',
        menu: {
            file: {
                title: 'File',
                items: 'preview'
            },
            edit: {
                title: 'Edit',
                items: 'undo redo | cut copy paste | selectall'
            },
            view: {
                title: 'View',
                items: 'code fullscreen'
            },
            insert: {
                title: 'Insert',
                items: 'image link media codesample | charmap emoticons hr'
            },
            format: {
                title: 'Format',
                items: 'bold italic underline strikethrough superscript subscript codeformat | formats blockformats fontformats fontsizes align lineheight | forecolor backcolor'
            },
            table: {
                title: 'Table',
                items: 'inserttable | cell row column | tableprops deletetable'
            }
        },
        toolbar_mode: 'sliding',
        toolbar: 'fontselect fontsizeselect | alignleft aligncenter alignright | bold italic underline | forecolor backcolor | bullist numlist | indent outdent',
        plugins: 'autoresize charmap code codesample emoticons fullscreen hr image imagetools link lists media nonbreaking preview table ',
        images_upload_url: '/upload-file',
        images_upload_base_path: 'http://localhost:9090',
        automatic_uploads: false,
        relative_urls: false
    });
});