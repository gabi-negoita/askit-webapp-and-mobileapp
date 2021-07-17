$(document).ready(function () {

    let createCategoryButton = $('#create-category');
    let titleInput = $('#title');

    // Show reject modal
    createCategoryButton.on('click', function () {
        $('#create-category-modal').modal({inverted: true}).modal('show');
    });

    // Handle invalid input
    titleInput.on('keyup', function () {
        let currentTitle = $(this).val();

        if (currentTitle.length < 2) createCategoryButton.addClass('disabled');
        else createCategoryButton.removeClass('disabled');
    });
});