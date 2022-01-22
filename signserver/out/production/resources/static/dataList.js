if (window.addEventListener) {
    window.addEventListener('load', InitEvent, false);
}

var selectAllBtn;

function InitEvent() {
    selectAllBtn    = document.getElementById('checkall');

    selectAllBtn.addEventListener('click', selectAllCheckBox);
}


function selectAllCheckBox() {
    console.log("selectAllCheckBox function called");
    var checkBoxes = document.getElementsByName("chk");
    for (var checkbox in checkBoxes) {
        console.log("checkboxes");
        checkbox.checked = source.checked;
    }
}