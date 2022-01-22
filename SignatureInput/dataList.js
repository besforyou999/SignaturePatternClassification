if (window.addEventListener) {
    window.addEventListener('load', InitEvent, false);
}

var toHomeBtn;

function InitEvent() {
    toHomeBtn = document.getElementById('toHomeBtn');
    toHomeBtn.addEventListener('click', returnToMain);
}

function returnToMain() {
        location.href = "/";
}