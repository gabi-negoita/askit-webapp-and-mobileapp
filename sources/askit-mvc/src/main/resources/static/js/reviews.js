$(document).ready(function () {

    let questionsSlider = $('#questions-slider');
    let answersSlider = $('#answers-slider');

    // Initialize slider
    questionsSlider.slider({
        min: 1,
        max: 60,
        start: 5,
        step: 1,
        smooth: true,
        onMove: function (value) {
            let label = value;
            if (value === 1) label += ' minute';
            else label += ' minutes';
            $('#questions-time-label').text(label);
        },
        onChange: function (value) {
            getUnreviewedQuestions(value);
        },
        interpretLabel: function (value) {
            if ((value + 1) === 1) return 1;
            if ((value + 1) % 5 !== 0) return '';

            return value + 1;
        }
    });
    answersSlider.slider({
        min: 1,
        max: 60,
        start: 5,
        step: 1,
        smooth: true,
        onMove: function (value) {
            let label = value;
            if (value === 1) label += ' minute';
            else label += ' minutes';
            $('#answers-time-label').text(label);
        },
        onChange: function (value) {
            getUnreviewedAnswers(value);
        },
        interpretLabel: function (value) {
            if ((value + 1) === 1) return 1;
            if ((value + 1) % 5 !== 0) return '';

            return value + 1;
        }
    });

    getUnreviewedQuestions(questionsSlider.slider('get value'));
    getUnreviewedAnswers(answersSlider.slider('get value'));

    // Update unreviewed posts count periodically
    clearInterval();
    setInterval(function () {
        getUnreviewedQuestions(questionsSlider.slider('get value'));
        getUnreviewedAnswers(answersSlider.slider('get value'));
    }, 10 * 1000);

    function getUnreviewedQuestions(minutes) {
        // Get unreviewed questions
        $.ajax({
            type: 'POST',
            url: '/home/reviews/unreviewed-count',
            data: {'type': 'questions', 'minutes': minutes},
            success: function (response) {
                // Check for empty response
                if (!$.trim(response)) return;

                // Update value
                let postLabel = 'post';
                if (response !== 1) postLabel += 's';
                $('#questions-post-label').text(postLabel);
                $('#questions-value').text(response);
            },
            fail: function (error) {
                alert('Failed: ' + error);
            }
        });
    }

    function getUnreviewedAnswers(minutes) {
        // Get unreviewed answers
        $.ajax({
            type: 'POST',
            url: '/home/reviews/unreviewed-count',
            data: {'type': 'answers', 'minutes': minutes},
            success: function (response) {
                // Check for empty response
                if (!$.trim(response)) return;

                // Update value
                let postLabel = 'post';
                if (response !== 1) postLabel += 's';
                $('#answers-post-label').text(postLabel);
                $('#answers-value').text(response);
            },
            fail: function (error) {
                alert('Failed: ' + error);
            }
        });
    }
});