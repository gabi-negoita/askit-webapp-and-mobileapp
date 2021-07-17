$(document).ready(function () {

    let calendar = $('#month-year-calendar');

    let categories = [];

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
            url: '/home/categories/most-used',
            data: {
                'date': date,
            },
            success: function (result) {
                categories = result;
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
        dataArray.push(["Category", "Posts"]);

        if (categories.length === 0) dataArray.push(['', 0]);
        categories.forEach(item => dataArray.push([item.categoryTitle, item.posts]));

        let data = new google.visualization.arrayToDataTable(dataArray);

        let view = new google.visualization.DataView(data);

        let options = {
            legend: {position: 'none'},
            displayAnnotations: false,
            displayDateBarSeparator: false,
            displayRangeSelector: false,
            displayZoomButtons: false,
            colors: ['#00B5AD'],
        }

        let chart = new google.visualization.ColumnChart(document.getElementById("most-used-categories-chart"));
        chart.draw(view, options);

        google.visualization.events.addListener(chart, 'select', selectHandler);

        function selectHandler() {
            let selectedCategoryTitle = data.getValue(chart.getSelection()[0].row, 0);

            let selectedCategory = categories.find(item => {
                return item.categoryTitle === selectedCategoryTitle;
            });

            window.location.href = "/home/categories/manage-category/" + selectedCategory.id;
        }
    }

    $(window).resize(function () {
        drawChart();
    });
});