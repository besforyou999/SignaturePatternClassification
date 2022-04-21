
if (window.addEventListener) {
    window.addEventListener('load', InitEvent, false);
}

var form;
var canvas, context ,tool, saveBtn, clearBtn, readBtn;
var SignatureData = [];

const state = {
      mousedown: false
    };

var deleteBorderBtn;

function InitEvent() {
    form            = document.querySelector("form");
    canvas          = document.getElementById('canvas');
    canvasContext   = canvas.getContext('2d');
    clearBtn        = document.getElementById('ClearBtn');
    saveBtn         = document.getElementById('SaveBtn');
    readBtn         = document.getElementById('ReadBtn');


    //tool = new tool_pencil();
    canvas.addEventListener('mousedown', handleWritingStart);
    canvas.addEventListener('mousemove', handleWritingInProgress);
    canvas.addEventListener('mouseup', handleDrawingEnd);
    canvas.addEventListener('touchstart',  handleWritingStart);
    canvas.addEventListener('touchmove', handleWritingInProgress);
    canvas.addEventListener('touchend', handleDrawingEnd);

    clearBtn.addEventListener('click',  onClear);
    saveBtn.addEventListener('click', save);
    readBtn.addEventListener('click', buildDataList);
}
function handleWritingStart(event) {
  event.preventDefault();

  const mousePos = getMousePositionOnCanvas(event);

  canvasContext.beginPath();

  canvasContext.moveTo(mousePos.x, mousePos.y);

  canvasContext.fill();

  state.mousedown = true;
}

function handleWritingInProgress(event) {
  event.preventDefault();

  if (state.mousedown) {
    const mousePos = getMousePositionOnCanvas(event);

    canvasContext.lineTo(mousePos.x, mousePos.y);
    canvasContext.stroke();
  }
}

function handleDrawingEnd(event) {
  event.preventDefault();

  if (state.mousedown) {
    canvasContext.stroke();
  }

  state.mousedown = false;
}


function getMousePositionOnCanvas(event){
    const clientX =event.clientX || event.touches[0].clientX;
    const clientY = event.clientY || event.touches[0].clientY;
    const {offsetLeft, offsetTop} = event.target;
    const canvasX=clientX-offsetLeft;
    const canvasY=clientY-offsetTop;

    return {x: canvasX, y: canvasY};

}

function onClear() {
	canvasContext.clearRect(0, 0, canvas.width, canvas.height);
	canvasContext.restore();
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

/*
function bodyScrollDisable(){
    document.body.style.overflow="hidden";
}

function bodyScrollAble(){
    document.body.style.overflow="auto";
}
*/
/*function tool_pencil() {
    var tool = this;
    this.started = false;

    this.mousedown = function (e) {
        context.beginPath();
        context.moveTo(e._x, e._y);
        tool.started = true;

        //coord.push([e._x, e._y]);
    };

    this.mousemove = function(e) {
        if (tool.started) {
            context.lineTo(e._x, e._y);
            context.stroke();

           // coord.push([e._x, e._y]);
        }
    };

    this.mouseup = function (e) {
        if (tool.started) {
            tool.mousemove(e);
            tool.started = false;
        }
    };

    this.touchstart = function (e) {
        e.preventDefault();
        const mousePos = getMousePositionOnCanvas(e);
        context.beginPath();
        context.moveTo(mousePos.x, mousePos.y);
        tool.started = true;

    };

    this.touchmove = function (e) {
        e.preventDefault();
        if (tool.started) {
        const mousePos=getMousePositionOnCanvas(e);
            context.lineTo(mousePos.x, mousePos.y);
            context.stroke();


        }
    };

    this.touchend = function (e) {
        e.preventDefault();
        if (tool.started) {
            tool.touchmove(e);
            tool.started=false;
         //bodyScrollAble();
        }
    };
}*/
/*
function ev_canvas(ev) {
    if (ev.offsetX || ev.offsetY == 0) { // Firefox 브라우저
	ev._x = ev.offsetX;
	ev._y = ev.offsetY;
    }

    // tool의 이벤트 핸들러를 호출한다.
    var func = tool[ev.type];
    if (func) {
	func(ev);
    }
}*/
