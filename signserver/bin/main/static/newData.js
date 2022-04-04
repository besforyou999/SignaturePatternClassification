/*
배열 SignatureData의 한 원소가 가지고 있는 데이터 ( 인덱스 순 )

서명의 검은색 선이 이루는 좌표값 배열, 서명의 분류, 서명의 URL

- 좌표값 배열 

    좌표값 배열의 한 원소는 x,y 좌표이고 길이 2인 정수형 배열로 이루어져있다.
    x, y값은 정수(int)이고 범위는 0 이상 300 이하

- 서명의 분류

    정수값 0,1,2,3 중 하나로 되어있다.
    0 == unknown , 1 == Number , 2 == Korean, 3 == English

- 서명의 URL

    서명의 이미지가 URL로 저장 되어있다. ( type은 string )
*/

if (window.addEventListener) {
    window.addEventListener('load', InitEvent, false);
}

var form;
var canvas, context ,tool, saveBtn, clearBtn, readBtn;
var SignatureData = [], coord = [];

var deleteBorderBtn;

function InitEvent() {
    form        = document.querySelector("form");
    canvas      = document.getElementById('canvas');
    context     = canvas.getContext('2d');
    clearBtn    = document.getElementById('ClearBtn');
    saveBtn     = document.getElementById('SaveBtn');
    readBtn     = document.getElementById('ReadBtn');


    tool = new tool_pencil();
    canvas.addEventListener('mousedown', ev_canvas, false);
    canvas.addEventListener('mousemove', ev_canvas, false);
    canvas.addEventListener('mouseup', ev_canvas, false);
    canvas.addEventListener('touchstart', ev_canvas, false);
    canvas.addEventListener('touchmove', ev_canvas, false);
    canvas.addEventListener('touchend', ev_canvas, false);
    clearBtn.addEventListener('click',  onClear);
    saveBtn.addEventListener('click', save);
    readBtn.addEventListener('click', buildDataList);
}

function tool_pencil() {
    var tool = this;
    this.started = false;

    this.mousedown = function (e) {
        context.beginPath();
        context.moveTo(e._x, e._y);
        tool.started = true;

        coord.push([e._x, e._y]);
    };

    this.mousemove = function(e) {
        if (tool.started) {
            context.lineTo(e._x, e._y);
            context.stroke();

            coord.push([e._x, e._y]);
        }
    };

    this.mouseup = function (e) {
        if (tool.started) {
            tool.mousemove(e);
            tool.started = false;
        }
    };

    this.touchstart = function (e) {
        bodyScrollDisable();
        context.beginPath();
        context.moveTo(e._x, e._y);
        tool.started = true;

    };

    this.touchmove = function (e) {
        if (tool.started) {
            context.lineTo(e._x, e._y);
            context.stroke();

        }
    };

    this.touchend = function (e) {
        if (tool.started) {
            tool.touchmove(e);
         bodyScrollAble();
        }
    };
}

function ev_canvas(ev) {
    if (ev.layerX || ev.layerX == 0) { // Firefox 브라우저
	ev._x = ev.offsetX;
	ev._y = ev.offsetY;
    }

    // tool의 이벤트 핸들러를 호출한다.
    var func = tool[ev.type];
    if (func) {
	func(ev);
    }
}

function onClear() {
	context.clearRect(0, 0, canvas.width, canvas.height);
	context.restore();
}


function save() {

    var imgUrl = canvas.toDataURL('image/png'); // typeof imgUrl == string
    var blobBin = atob(imgUrl.split(',')[1]);   // base64 데이터 디코딩
    var array = [];

    for (var i = 0; i < blobBin.length ; i++) {
        array.push(blobBin.charCodeAt(i));
    }

    var file = new Blob([new Uint8Array(array)], {type: 'image/png'});

    var num;
    var objLen = document.getElementsByName('classify').length;
    for (var i = 0 ; i < objLen ; i++) {
        var curObj = document.getElementsByName('classify')[i];
        if (curObj.checked == true) {
            num = String(curObj.value);
            break;
        }
    }

    var numBlob = new Blob([num], {type: 'text/plain'});

    var formdata = new FormData();
    formdata.append("file", file);
    formdata.append("file", numBlob);

    $.ajax({
            type : 'post',
            url : '/saveImage',
            data : formdata,
            processData : false,	// data 파라미터 강제 string 변환 방지!!
            contentType : false,	// application/x-www-form-urlencoded; 방지!!
            success : function (data) {
                alert("완료!")
            }
    });
    onClear();
}

function buildDataList() {
    if (confirm("read data from database and build list?")) location.href = "/dataList";
}

function bodyScrollDisable(){
    document.body.style.overflow="hidden";
}

function bodyScrollAble(){
    document.body.style.overflow="auto";
}
