$(document).ready(function () {
    // Post answer
    $('#submit-button').on('click', function (e) {
        // Prevent form from submitting
        e.preventDefault();

        if (tinyMCE.get('body').getContent() === '') {
            // Submit form with errors to generate error messages
            $('#edit-answer-form').submit();
        }

        // upload images
        tinymce.activeEditor.uploadImages(function () {
            // upload question
            $('#edit-answer-form').submit();
        });
    });
});