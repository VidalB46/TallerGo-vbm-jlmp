document.addEventListener('DOMContentLoaded', function () {
    const backButtons = document.querySelectorAll('.js-back-button');

    backButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            window.history.back();
        });
    });
});