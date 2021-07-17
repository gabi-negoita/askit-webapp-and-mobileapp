$(document).ready(function () {

    let calendar = $('#month-year-calendar');

    let users = [];

    let today = new Date();
    let mm = today.getMonth() + 1;
    if (mm < 10) mm = '0' + mm;
    let yyyy = today.getFullYear();
    loadData(yyyy + '-' + mm);

    calendar.calendar({
        type: 'month',
        formatter: {
            date: function (date) {
                if (!date) return '';

                let month = (date.getMonth() + 1) + '';
                if (month.length < 2) month = '0' + month;

                let year = date.getFullYear();

                return year + "-" + month;
            }
        },
        popupOptions: {
            position: 'bottom center',
            transition: 'slide down',
            lastResort: 'bottom left',
            prefer: 'opposite',
            hideOnScroll: false,
            observeChanges: false
        },
        initialDate: new Date(today.getFullYear(), today.getMonth(), today.getDate()),
        maxDate: new Date(today.getFullYear(), today.getMonth(), today.getDate()),
        onChange: function (date, value) {
            loadData(value);
        }
    });

    $('#all-time').on('change', function () {
        let isChecked = this.checked;

        if (isChecked) {
            calendar.addClass("disabled");
            loadData('');
            return;
        }

        calendar.removeClass("disabled");
        loadData($('#date-input').val());
    });

    function loadData(date) {
        $.ajax({
            type: 'POST',
            url: '/home/users/most-active',
            data: {
                'date': date,
            },
            success: function (result) {
                users = result;
                startRendering();
            },
            fail: function () {
                alert('Failed');
            }
        });
    }

    function startRendering() {
        // Draw chart
        google.charts.load("current", {packages: ['corechart']});
        google.charts.setOnLoadCallback(drawChart);
    }

    function drawChart() {
        let dataArray = [];
        dataArray.push(["User", "Contributions"]);

        if (users.length === 0) dataArray.push(['', 0]);
        users.forEach(item => dataArray.push([item.username, item.contributions]));

        let data = new google.visualization.arrayToDataTable(dataArray);

        let view = new google.visualization.DataView(data);

        let options = {
            legend: {position: 'none'},
            displayAnnotations: false,
            displayDateBarSeparator: false,
            displayRangeSelector: false,
            displayZoomButtons: false,
        }

        let chart = new google.visualization.ColumnChart(document.getElementById("most-active-users-chart"));
        chart.draw(view, options);

        google.visualization.events.addListener(chart, 'select', selectHandler);

        function selectHandler() {
            let selectedUsername = data.getValue(chart.getSelection()[0].row, 0);

            let selectedUser = users.find(item => {
                return item.username === selectedUsername;
            });

            window.location.href = "/home/users/manage-user/" + selectedUser.id;
        }
    }

    $(window).resize(function () {
        drawChart();
    });
});