if (window.addEventListener) {
    window.addEventListener('load', InitEvent, false);
}

var canvas, context ,tool, finishBtn, clearBtn;

function InitEvent() {
    canvas = document.getElementById('canvas');
    context = canvas.getContext('2d');
    clearBtn = document.getElementById('clearBtn');

    tool = new tool_pencil();
    canvas.addEventListener('mousedown', ev_canvas, false);
    canvas.addEventListener('mousemove', ev_canvas, false);
    canvas.addEventListener('mouseup', ev_canvas, false);
    canvas.addEventListener('touchstart', ev_canvas, false);
    canvas.addEventListener('touchmove', ev_canvas, false);
    canvas.addEventListener('touchend', ev_canvas, false);
    clearBtn.addEventListener('click',  printAlert);

}

function tool_pencil() {
    var tool = this;
    this.started = false;

    this.mousedown = function (e) {
        context.beginPath();
        context.moveTo(e._x, e._y);
        tool.started = true;
    };

    this.mousemove = function(e) {
        if (tool.started) {
            context.lineTo(e._x, e._y);
            context.stroke();
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
    canvas = document.getElementById('Canvas');
    context.save();
	context.setTransform(1, 0, 0, 1, 0, 0);
	context.clearRect(0, 0, canvas.width, canvas.height);
	context.restore();
}

function printAlert() {
    context.clearRect(0, 0, canvas.width, canvas.height);
	context.restore();
}