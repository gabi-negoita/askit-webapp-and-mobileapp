$(document).ready(function () {
    let search = $('.ui.search');
    let userSearch = $('#user-search');
    let userInput = $('#user-input');

    search.search({
        source: [],
        searchFields: [
            'title',
            'description'
        ],
        fullTextSearch: true
    });


    // Search for user
    userInput.on('keyup', function (event) {
        // Enable search input loading animation
        userSearch.addClass('loading');

        // Get data
        let username = $(this).val();

        // Check if string is empty
        if (!username) {
            userSearch.removeClass('loading');
            return;
        }

        // Get users
        $.ajax({
            type: 'POST',
            url: '/home/send-notification/api/users',
            data: {'username': username},
            success: function (response) {
                let newContent = [];
                response.forEach(function (item) {
                    newContent.push({
                        title: item.id,
                        description: item.username + ' (' + item.email + ')',
                    });
                });

                search.search('setting', 'source', newContent);
                userSearch.removeClass('loading');
            },
            fail: function (res) {
                alert('Failed: ' + res);
            }
        });
    });

    // Toggle user dropdown based on checkbox
    $('#broadcast').on('change', function () {
        let isChecked = this.checked;

        if (isChecked) userInput.prop("disabled", true);
        else userInput.prop("disabled", false);
    });
})