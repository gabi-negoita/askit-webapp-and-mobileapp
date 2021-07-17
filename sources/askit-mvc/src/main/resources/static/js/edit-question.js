$(document).ready(function () {
    // Handle form submit
    $('#submit-button').on('click', function (e) {
        // Prevent form from submitting
        e.preventDefault();

        // validate fields
        if ($('input[name ="subject"]').val().length === 0 ||
            $('input[name ="category"]').val().length === 0 ||
            tinyMCE.get('body').getContent() === '') {
            // Submit form with errors to generate error messages
            $('#edit-question-form').submit();
        }

        // upload images
        tinymce.activeEditor.uploadImages(function () {
            // upload question
            $('#edit-question-form').submit();
        });
    });
});