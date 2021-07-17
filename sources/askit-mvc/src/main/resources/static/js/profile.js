$(document).ready(function () {
    let activityList = [];
    getUserActivity();

    function getUserActivity() {
        let currentPath = window.location.pathname;
        let url = currentPath.substr(0, currentPath.lastIndexOf('/')) + '/api/user-activity';

        $.ajax({
            type: 'POST',
            url: url,
            data: {},
            success: function (response) {
                if (response.length === 0) {
                    let container = $('#chart');
                    container.addClass('ui info message');
                    container.html('<div class="header">No activity</div>');
                    return;
                }

                activityList = response;

                // Init google charts
                google.charts.load('current', {'packages': ['annotatedtimeline']});
                google.charts.setOnLoadCallback(drawChart);
            },
            fail: function (res) {
                alert('Failed: ' + res);
            }
        });
    }

    function drawChart() {
        // Add columns
        let data = new google.visualization.DataTable();
        data.addColumn('date', 'Date');
        data.addColumn('number', 'Answers posted');
        data.addColumn('number', 'Questions posted');

        // Add rows
        activityList.forEach(item => data.addRow([new Date(item.date), item.answers, item.questions]));

        // Display chart
        let chart = new google.visualization.AnnotatedTimeLine(document.getElementById('chart'));
        let options = {
            colors: ['#6AB950', '#D8782B'],
            thickness: 2,
            scaleType: 'maximized'
        }
        chart.draw(data, options);
    }

    // Redraw chart on window resize
    $(window).resize(function () {
        drawChart();
    });
});