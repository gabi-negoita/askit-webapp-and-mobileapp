$(document).ready(function () {
    const SECOND = 1;
    const MINUTE = 60 * SECOND;
    const HOUR = 60 * MINUTE;
    const DAY = 24 * HOUR;
    const MONTH = 30 * DAY;
    const YEAR = 12 * MONTH;

    let notificationsBody = $('#notifications-body');
    let notificationIcon = $('#notification-icon');
    let allViewed = true;
    let notified = false;

    function getRelativeTime(rawDate) {

        const date = new Date(rawDate);
        const dateInMilliseconds = date.getTime();
        const secondsPassed = Math.floor((Date.now() - dateInMilliseconds) / 1000);

        // Get seconds
        if (secondsPassed < MINUTE) return `less than a minute ago`;

        // Get within 2 minutes
        if (secondsPassed < 2 * MINUTE) return `a minute ago`;

        // Get minutes
        if (secondsPassed < HOUR) return `${Math.floor(secondsPassed / MINUTE)} minutes ago`;

        // Get within 1 hour
        if (secondsPassed < 2 * HOUR) return `an hour ago`;

        // Get hours
        if (secondsPassed < DAY) return `${Math.floor(secondsPassed / HOUR)} hours ago`;

        // Get within 2 days
        if (secondsPassed < 2 * DAY) return `yesterday`;

        // Get days
        if (secondsPassed < MONTH) return `${Math.floor(secondsPassed / DAY)} days ago`;

        // Get months
        if (secondsPassed < 2 * MONTH) return `a month ago`;

        // Get months
        if (secondsPassed < YEAR) return `${Math.floor(secondsPassed / MONTH)} months ago`;

        // Get within 2 years
        if (secondsPassed < 2 * YEAR) return `a year ago`;

        // Get years
        return `${Math.floor(secondsPassed / YEAR)} years ago`;
    }

    // Format date
    $("span[class='raw-date']").each(function () {
        // Get item
        let item = $(this);

        // Get item content
        let itemContent = item.html();

        // Change item content
        item.html(getRelativeTime(itemContent));
    });

    // Initialize user's dropdown menu
    $('.ui.dropdown.item').dropdown();

    // Show logout modal
    $("a.item.logout").click(function () {
        $('#logout-modal').modal({inverted: true}).modal('show');
    });

    // Initialize side bar
    $("#sidebar").click(function () {
        $('.ui.vertical.sidebar.menu').sidebar({
            'transition': 'push',
            dimPage: false
        }).sidebar('toggle');
    });

    // Set up notification popup
    $('.notification.item').popup({
        arrowPixelsFromEdge: 0,
        jitter: 0,
        position: 'bottom center',
        lastResort: 'bottom right',
        on: 'click'
    });

    // Get notifications periodically
    clearInterval();
    setInterval(function () {
        getNotifications();
    }, 10 * 1000);

    getNotifications();

    function getNotifications() {
        $.ajax({
            type: 'POST',
            url: '/home/profile/api/notifications',
            data: {},
            success: function (response) {
                // Check for empty response
                if (!$.trim(response)) return;

                // Generate notifications
                generateNotifications(response);
            },
            fail: function (error) {
                alert('Failed: ' + error);
            }
        });
    }

    function generateNotifications(data) {
        let content = ''

        data.forEach(function (item) {
            let statusLabel = '<div class="right floated middle aligned content">';
            if (item.viewed === null) statusLabel += '<i class="blue globe americas icon"></i>';
            else if (item.viewed === 0) {
                statusLabel += '<i class="blue small circle icon"></i>';
                allViewed = false;
            }
            statusLabel += '</div>';

            content += '<div class="item">' +
                statusLabel +
                '<div class="middle aligned content">' +
                '<a class="header" target="_blank" href="' + item.url + '">' + item.title + '</a>' +
                '</div>' +
                '<div class="description">' + getRelativeTime(item.createdDate) + '</div>' +
                '</div>';
        });

        notificationsBody.html(content);

        if (!allViewed) {
            if (notificationIcon.hasClass('outline')) notificationIcon.removeClass('outline');
            if (!notificationIcon.hasClass('yellow')) notificationIcon.addClass('yellow');
            notificationIcon.transition('tada');

            if (notified) return;

            // Show notification
            $('body').toast({
                title: 'New notifications',
                showIcon: 'bell',
                class: 'info',
                position: 'bottom right',
                displayTime: 5000,
                progressUp: true,
                showProgress: 'bottom',
                className: {
                    box: 'toast-box',
                }
            });

            notified = true;
        }
    }

    $('#mark-all-as-read').on('click', function () {
        $.ajax({
            type: 'POST',
            url: '/home/profile/api/notifications/mark-all-as-read',
            data: {},
            success: function (response) {
                // Check for empty response
                if (!$.trim(response)) return;

                // Toast message
                $('body').toast({
                    title: 'Notifications marked as read!',
                    showIcon: 'check double',
                    class: 'info',
                    position: 'bottom right',
                    displayTime: 5000,
                    progressUp: true,
                    showProgress: 'bottom',
                    className: {
                        box: 'toast-box',
                    }
                });

                // Change bell icon
                if (notificationIcon.hasClass('yellow')) notificationIcon.removeClass('yellow');
                if (!notificationIcon.hasClass('outline')) notificationIcon.addClass('outline');

                allViewed = true;

                // Generate notifications
                getNotifications();
            },
            fail: function (error) {
                alert('Failed: ' + error);
            }
        });
    });
});
