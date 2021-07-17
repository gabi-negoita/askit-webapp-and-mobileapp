$(document).ready(function () {
    let saveChangesButton = $('#save-changes');
    let deleteButton = $('#delete');
    let titleInput = $('#title');
    let oldTitle = titleInput.val();

    // Show reject modal
    saveChangesButton.on('click', function () {
        $('#edit-category-modal').modal({inverted: true}).modal('show');
    });

    // Handle invalid input
    titleInput.on('keyup', function () {
        let currentTitle = $(this).val();

        if (currentTitle.length < 2 || currentTitle === oldTitle) saveChangesButton.addClass('disabled');
        else saveChangesButton.removeClass('disabled');
    });

    deleteButton.on('click', function () {
        $('#delete-category-modal').modal({inverted: true}).modal('show');
    });
});