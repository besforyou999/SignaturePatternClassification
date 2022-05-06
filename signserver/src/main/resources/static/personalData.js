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
    sendBtn         = document.getElementById('sendBtn');

    //tool = new tool_pencil();
    canvas.addEventListener('mousedown', handleWritingStart);
    canvas.addEventListener('mousemove', handleWritingInProgress);
    canvas.addEventListener('mouseup', handleDrawingEnd);
    canvas.addEventListener('touchstart',  handleWritingStart);
    canvas.addEventListener('touchmove', handleWritingInProgress);
    canvas.addEventListener('touchend', handleDrawingEnd);
    clearBtn.addEventListener('click',  onClear);
    //save -> send로 바꾸기
    sendBtn.addEventListener('click', send);
}
var isFirst = true;

var signData = new Object();
var today = new Date();
var xArray = new Array();
var yArray= new Array();
var timeArray = new Array();
var frstArray = new Array();

var timer;
// 인터페이스 수정..
function handleWritingStart(event) {
  event.preventDefault();

  const mousePos = getMousePositionOnCanvas(event);
  canvasContext.beginPath();
  canvasContext.moveTo(mousePos.x, mousePos.y);
  canvasContext.fill();

  signData.cnt=1;
  xArray.push(mousePos.x);
  yArray.push(mousePos.y);
  timeArray.push(Math.ceil(Date.now()/10)*10);
  frstArray.push(0);



   //setTimeout(()=>{clearInterval(timer); alert('시간 초과!');}, 5000)

    //  frstArray.push(1);

  state.mousedown = true;
}

function handleWritingInProgress(event) {
  event.preventDefault();

  if (state.mousedown) {
    const mousePos = getMousePositionOnCanvas(event);
    canvasContext.lineTo(mousePos.x, mousePos.y);
    canvasContext.stroke();

    timer = setInterval(arrayPush(event), 10);

  }
}
function arrayPush(event){
       event.preventDefault();

       if(state.mousedown){
       const mousePos = getMousePositionOnCanvas(event);

       signData.cnt++;
       xArray.push(mousePos.x);
       yArray.push(mousePos.y);
       timeArray.push(Date.now());
       frstArray.push(1);
       }
}

function handleDrawingEnd(event) {
  event.preventDefault();

  if (state.mousedown) {
    canvasContext.stroke();
  }

    signData.xPos=xArray;
    signData.yPos=yArray;
    signData.time=timeArray;
    signData.isFirst=frstArray;

    state.mousedown = false;
    isFirst=true;
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

	clearInterval(timer);
}

function send() {
/*$.ajax({
            type : 'post',
            url : '/sendPersonal',
            data : signData,
            processData : false,
            contentType : false,
            async : false,
            success : function (rslt) {
                $('#result').text(rslt)
                alert(rslt); //디버깅용 나중에 완성시 지워도됩니다~~
            }
    });*/
    console.log(signData);
    signData=new Object();
    clearInterval(timer);
    onClear();
}

/*
send 내용..
    var imgUrl = canvas.toDataURL('image/png'); // typeof imgUrl == string
    var blobBin = atob(imgUrl.split(',')[1]);   // base64 데이터 디코딩
    var array = [];

    for (var i = 0; i < blobBin.length ; i++) {
        array.push(blobBin.charCodeAt(i));
    }

    var file = new Blob([new Uint8Array(array)], {type: 'image/png'});

    var formdata = new FormData();
    formdata.append("file", file);

    $.ajax({
            type : 'post',
            url : '/sendImage',
            data : formdata,
            processData : false,
            contentType : false,
            async : false,
            success : function (rslt) {
                $('#result').text(rslt)
                alert(rslt); //디버깅용 나중에 완성시 지워도됩니다~~
            }
    });
*/
