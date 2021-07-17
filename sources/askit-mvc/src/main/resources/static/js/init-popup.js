$(document).ready(function () {
    $('div[data-content]').popup({
        setFluidWidth: true,
        position: 'top center',
        hoverable: true,
        transition: 'zoom',
        title: 'Posted on'
    });
});