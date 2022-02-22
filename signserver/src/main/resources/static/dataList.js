var deleteBorderBtn;
deleteBorderBtn = document.getElementById('deleteBorderBtn');
deleteBorderBtn.addEventListener('click', testFunction);

function testFunction() {
    alert("Hello world!");
}

function deleteImg(id) {
    $.ajax({
        type : 'post',
        url : '/deleteSign',
        data : { "id" : id},
        success : function (data) {
            alert("완료!");
            location.reload();
        },
        error : function() {
        	alert("error in delete Img");
        }
    });
}

function saveChangeLabel(value) {
    var dropDown = document.getElementById(value);
    var selection = dropDown.options[dropDown.selectedIndex].value;
    $.ajax({
        type : 'post',
        url : '/changeLabel',
        data : { "label" : selection , "id" : value},
        success : function (data) {
            alert("완료!");
            location.reload();
        },
        error : function() {
            alert("error in save Change Label");
        }
    });
}