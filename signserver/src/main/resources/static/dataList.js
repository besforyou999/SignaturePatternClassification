/*
버튼 눌렸을 때, /deleteImage 로 넘어가면서 id 만 전달해주면 될 것 같은데,
애초에 html 데이터 테이블을 여기서 어떻게 가져오는지를 몰라서 크흡 ㅠㅠㅠ
파일만 만들어서 보냅니다 ..
그리고 서버 실행하는데 필요 없는 부분들도 다 주석처리해놨어여..ㅎ
*/

function deleteImg(id) {

    $.ajax({
        type : 'post',
        url : '/deleteSign',
        data : { "id" : id},
        success : function (data) {
            alert("완료!");
            location.reload();
        }
    });


}
