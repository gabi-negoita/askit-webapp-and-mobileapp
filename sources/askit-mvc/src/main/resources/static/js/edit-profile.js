$(document).ready(function () {
    // Initialize calendar
    $('.ui.calendar').calendar({
        type: 'date',
        formatter: {
            date: function (date) {
                if (!date) return '';

                let day = date.getDate() + '';
                if (day.length < 2) day = '0' + day;

                let month = (date.getMonth() + 1) + '';
                if (month.length < 2) month = '0' + month;

                let year = date.getFullYear();

                return year + "-" + month + "-" + day;
            }
        },
        popupOptions: {
            position: 'bottom center',
            transition: 'slide down',
            lastResort: 'bottom left',
            prefer: 'opposite',
            hideOnScroll: false,
            observeChanges: false
        }
    });
});