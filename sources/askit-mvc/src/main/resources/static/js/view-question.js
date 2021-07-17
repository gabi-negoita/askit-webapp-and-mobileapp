$(document).ready(function () {
    // Post answer
    $('#submit-button').on('click', function (e) {
        // Prevent form from submitting
        e.preventDefault();

        if (tinyMCE.get('body').getContent() === '') {
            // Submit form with errors to generate error messages
            $('#post-answer').submit();
            return;
        }

        // upload images
        tinymce.activeEditor.uploadImages(function () {
            // upload question
            $('#post-answer').submit();
        });
    });

    // Handle question vote
    $('.item.question-vote').on('click', function () {
        let item = $(this);
        let questionId = item.attr('value');
        let vote = toggleItem(item);

        $.ajax({
            type: 'POST',
            url: '/questions/vote',
            data: {
                'vote': vote,
                'questionId': questionId
            },
            success: function () {
                $('body').toast({
                    title: vote === 0 ? 'Vote removed!' : 'Vote submitted!',
                    showIcon: vote === 0 ? 'times' : 'vote yea',
                    class: vote === 0 ? 'black' : (vote === 1 ? 'green' : 'orange'),
                    position: 'bottom right',
                    displayTime: 5000,
                    progressUp: true,
                    showProgress: 'bottom',
                    className: {
                        box:'toast-box',
                    }
                });
            },
            fail: function () {
                $('body').toast({
                    title: 'Vote could not be submitted. Please try again!',
                    showIcon: 'exclamation circle',
                    class: 'error',
                    position: 'bottom right',
                    displayTime: 5000,
                    progressUp: true,
                    showProgress: 'bottom',
                    className: {
                        box:'toast-box',
                    }
                });
                toggleItem(item);
            }
        });
    });

    // Handle answer vote
    $('.item.answer-vote').on('click', function () {
        let item = $(this);
        let answerId = item.attr('value');
        let vote = toggleItem(item);

        $.ajax({
            type: 'POST',
            url: '/answers/vote',
            data: {
                'vote': vote,
                'answerId': answerId
            },
            success: function () {
                $('body').toast({
                    title: vote === 0 ? 'Vote removed!' : 'Vote submitted!',
                    showIcon: vote === 0 ? 'times' : 'vote yea',
                    class: vote === 0 ? 'black' : (vote === 1 ? 'green' : 'orange'),
                    position: 'bottom right',
                    displayTime: 5000,
                    progressUp: true,
                    showProgress: 'bottom',
                    className: {
                        box:'toast-box',
                    }
                });
            },
            fail: function () {
                $('body').toast({
                    title: 'Vote could not be submitted. Please try again!',
                    showIcon: 'exclamation circle',
                    class: 'error',
                    position: 'bottom right',
                    displayTime: 5000,
                    progressUp: true,
                    showProgress: 'bottom',
                    className: {
                        box:'toast-box',
                    }
                });
                toggleItem(item);
            }
        });
    });

    function toggleItem(item) {
        let itemLabel = item.children('span').html();
        let itemSibling = item.siblings('a');
        let vote;

        switch (itemLabel) {
            case 'Up vote': {
                // Change state
                item.addClass('header');
                item.children('span').html('Up voted');
                item.children('i').addClass('green');

                let diffVote = 1;
                if (itemSibling.children('i').hasClass('orange')) {
                    diffVote++;
                }

                // Change sibling state
                itemSibling.children('span').html('Down vote');
                itemSibling.children('i').removeClass('orange');

                // Set vote
                vote = 1;

                // Update votes
                updateVotes(item, diffVote);
                break;
            }
            case 'Up voted': {
                // Change state
                item.removeClass('header');
                item.children('span').html('Up vote');
                item.children('i').removeClass('green');

                // Set vote
                vote = 0;

                // Update votes
                updateVotes(item, -1);
                break;
            }
            case 'Down vote': {
                // Change state
                item.addClass('header');
                item.children('span').html('Down voted');
                item.children('i').addClass('orange');

                let diffVote = -1;

                if (itemSibling.children('i').hasClass('green')) {
                    diffVote--;
                }

                // Change sibling state
                itemSibling.children('span').html('Up vote');
                itemSibling.children('i').removeClass('green');

                // Set vote
                vote = -1;

                // Update votes
                updateVotes(item, diffVote);
                break;
            }
            case 'Down voted': {
                // Change state
                item.removeClass('header');
                item.children('span').html('Down vote');
                item.children('i').removeClass('orange');

                // Set vote
                vote = 0;

                // Update votes
                updateVotes(item, 1);
                break;
            }
        }

        return vote;
    }

    function updateVotes(item, newVote) {
        let votesLabel = item.siblings('div').children('div').children('span');
        let votes = parseInt(votesLabel.html().split(' ')[0]);
        let label = votesLabel.html().split(' ')[1];

        votesLabel.html((votes + newVote) + ' ' + label);
    }
});