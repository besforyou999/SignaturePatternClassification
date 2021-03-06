if (window.addEventListener) {
    window.addEventListener('load', InitEvent, false);
}

var form;
var canvas, context ,tool, saveBtn, clearBtn, readBtn, registerBtn;
var SignatureData = [];
const state = {
      mousedown: false
    };

var isFirst = true;

//시간 얻기 위한 객체 선언
var today = new Date();

//sign Data 선언
var signData, xArray, yArray, timeArray, frstArray;


//몇 번째 점마다 push 할지 결정하는 변수
var inputCnt;

function InitEvent() {
    form            = document.querySelector("form");
    canvas          = document.getElementById('canvas');
    canvasContext   = canvas.getContext('2d');
    clearBtn        = document.getElementById('ClearBtn');
    registerBtn     = document.getElementById('registerBtn');
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
    initSignData();
}

function initSignData(){
    signData = new Object;
    xArray = new Array();
    yArray = new Array();
    timeArray = new Array();
    frstArray = new Array();
}


// 서명 시작
function handleWritingStart(event) {
  event.preventDefault();
  const mousePos = getMousePositionOnCanvas(event);
  canvasContext.beginPath();
  canvasContext.moveTo(mousePos.x, mousePos.y);
  canvasContext.fill();

  //signData.cnt++;
  xArray.push(mousePos.x);
  yArray.push(mousePos.y);
  timeArray.push(Date.now());
  frstArray.push(0);
  state.mousedown = true;
  //새로운 획마다 inputCnt 초기화
  //inputCnt=0;
}

//서명 중
function handleWritingInProgress(event) {
  event.preventDefault();

  if (state.mousedown) {
    const mousePos = getMousePositionOnCanvas(event);
    canvasContext.lineTo(mousePos.x, mousePos.y);
    canvasContext.stroke();
    //inputCnt++;
    //if(inputCnt%8==0)
    arrayPush(event);
  }
}
//배열에 값 넣는 함수
function arrayPush(event){
       const mousePos = getMousePositionOnCanvas(event);
       //signData.cnt++;
       xArray.push(mousePos.x);
       yArray.push(mousePos.y);
       timeArray.push(Date.now());
       frstArray.push(1);

}
// 마우스 뗄 때
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
}

var sendData = new Object();
var sendIdx =0;
function register(){

    sendData[sendIdx++] = signData;
    initSignData();
    onClear();
    alert(sendIdx + "개 사인 등록");
}

// 수정해야함
function send() {

    sendData['name']= document.getElementById('name').value;

    //널체크
    if(sendData['name']=="")
    {
        alert("이름을 입력해주세요!")
    }
    else{
    //수정해야함.
    $.ajax({
            type : 'post',
            url : '/confirmDataDB',
            data : JSON.stringify(signData),
            processData : false,
            dataType:'json',
            contentType : 'application/json',
            async : false,
            success : function (rslt) {
                alert("서버로 전송");
            }

    });
    console.log(signData);
    initSignData();
    onClear();
    }

}

function initSendData(){
    document.getElementById('name').value="";
    delete sendData;
    sendData = new Object();
    sendIdx=0;
}
