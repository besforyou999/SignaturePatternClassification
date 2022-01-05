if (window.addEventListener) {
    window.addEventListener('load', InitEvent, false);
}

var form; // for radiobtn
var canvas, context ,tool, finishBtn, clearBtn;
var SignatureData = [], coord = [];

function InitEvent() {
    form      = document.querySelector("form");
    canvas    = document.getElementById('canvas');
    context   = canvas.getContext('2d');
    clearBtn  = document.getElementById('clearBtn');
    finishBtn = document.getElementById('finishBtn');

    tool = new tool_pencil();
    canvas.addEventListener('mousedown', ev_canvas, false);
    canvas.addEventListener('mousemove', ev_canvas, false);
    canvas.addEventListener('mouseup', ev_canvas, false);
    canvas.addEventListener('touchstart', ev_canvas, false);
    canvas.addEventListener('touchmove', ev_canvas, false);
    canvas.addEventListener('touchend', ev_canvas, false);
    clearBtn.addEventListener('click',  onClear);
    finishBtn.addEventListener('click', save);

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
            tool.started = false;
        }
    };
}

function ev_canvas(ev) {
    if (ev.layerX || ev.layerX == 0) { // Firefox 브라우저
	ev._x = ev.layerX;
	ev._y = ev.layerY;
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

    var sigInfo = [];

    // insert coord
    sigInfo.push(coord);
    coord = [];

    // insert label data
    var data = new FormData(form);
    var output;
    for (const entry of data) {
        output = entry[1];
    };
    sigInfo.push(output);

    SignatureData.push(sigInfo);

    onClear();


}
