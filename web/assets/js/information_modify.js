// JavaScript Document


var store_name = ''; //가게 상호명
var store_phone = ''; //가게 번호
var store_address = ''; //가게 주소
var store_point_x = ''; //주소의 x좌표
var store_point_y = ''; //주소의 y좌표
var start_time = ''; // 오픈시간
var end_time = ''; //마감시간
var storemaster_name = ''; //가게주인
var storemaster_phone = ''; //가게주인핸드폰번호
var storemaster_rrn = ''; //가게주 주민번호
var storemaster_num = ''; //사업자번호
var store_restday = '';
var store_notice = '';
var store_phone = '';
var Store_profile_img = '';
var store_serial='';
var minimum_cost='';
var delivery_cost='';


$(document).on("click", "#btn_modify", function (a) {
    minimum_cost = $("#cost_lower_bound").val();
    minimum_cost = 1*minimum_cost;
    mId2 = $("#store_id").val();
    mPassword2 = $("#store_pw").val();
    Store_name = $("#store_name").val(); //가게 상호명
    Store_phone = $("#store_phone").val(); //가게 번호
    Store_address = $("#store_address").val(); //가게 주소
    Start_time = $("#start_work_time").val(); // 오픈시간
    End_time = $("#end_work_time").val(); //마감시간
    Storemaster_name = $("#storemaster_name").val(); //가게주인
    Storemaster_phone = $("#storemaster_phone").val(); //가게주인핸드폰번호
    Storemaster_rrn = $("#storemaster_rrn").val(); //가게주 주민번호
    Storemaster_num = $("#storemaster_num").val(); //사업자번호
    Store_notice = $("#store_notice").val();
    Store_restday = $("#store_restday").val();
    delivery_cost = $("#delivery_cost").val();
    delivery_cost = delivery_cost*1;

    //alert(Start_time);
    //alert(End_time);
    //alert(typeof(Start_time));
    modifyData();

});

/*
function modifyData()
{
           var data2 = {
			   		"store_name" : Store_name, //가게 상호명
  					"store_phone" : Store_phone,  //가게 번호
  					"store_address" : Store_address,  //가게 주소
  					"start_time" : Start_time,  // 오픈시간
			   		"end_time" : End_time,  //마감시간
  					"storemaster_name" : Storemaster_name,  //가게주인
  					"store_phone" : Store_phone, //가게주인핸드폰번호
  					"storemaster_num" : Storemaster_num, //사업자번호
			   		"store_notice" : store_notice,
			   		
           };

           var json2 = JSON.stringify(data2);
           var http2 = new XMLHttpRequest();
           var url = "http://54.180.102.7:80/get/store_manage.php";

		   http2.open('POST', url, true);
           http2.setRequestHeader('Content-Type', 'application/json');
           http2.onerror = function (u) {
               alert("Server not response");
           }
		   console.log(json2);
		   
	
	     
           http1.send(json2);
	
           http1.onload = function () {
		   
               if (http2.readyState == 4) 
			   {
                   if (http2.status == 200)
				   {
                       var result2 = JSON.parse(http2.responseText);
				
                   }
                   else {
                       alert("Connect fail..");
                   }
               }
               else {
                   alert("Connect fail...");
               }
		   }

}

*/

function modifyData() {

    var temp = Store_profile_img.length;
    var image_check = "";
    image_check = image_check*1;
    if(temp==0)
        image_check=0;
    else
        image_check=1;

    var formdata = new FormData();
    formdata.append('image_file', Store_profile_img);
    formdata.append('img_check', image_check);
    formdata.append('store_serial', store_serial);
    formdata.append('store_info', "modify");
    formdata.append('store_name', Store_name);
    formdata.append('store_phone', Store_phone);
    formdata.append('store_address', Store_address);
    formdata.append('minimum_order_price', minimum_cost);
    formdata.append('start_time', Start_time);
    formdata.append('end_time', End_time);
    formdata.append('storemaster_name', Storemaster_name);
    formdata.append('store_phone', Store_phone);
    formdata.append('storemaster_num', Storemaster_num);
    formdata.append('store_notice', Store_notice);
    formdata.append('store_restday', Store_restday);
    formdata.append('delivery_cost',delivery_cost);

    $.ajax({
        url: "http://54.180.102.7:80/get/JSON/store_app/store_modify_info.php",
        data: formdata,
        dataType:'text',
        encrypt:"multipart/form-data",
        processData: false,
        contentType: false,
        type: "POST",
        success: function (data) {
            if (data) {
                var result3 = JSON.parse(data);
                if(result3.confirm==1)
				{
					$("#store_profile").attr("src",result3.path);
                	alert("정보 수정 성공");
                	location.reload();
				}
            } else {
                alert("수정하기 실패");
            }
        },
        error: function (error) 
		{
            alert(error);
        }
    });
    event.preventDefault();

}

$(document).ready(function () {
    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader(); //파일을 읽기 위한 FileReader객체 생성
            reader.onload = function (e) {
                //파일 읽어들이기를 성공했을때 호출되는 이벤트 핸들러
                $("#store_profile").attr("src", e.target.result);
                //alert(e.target.result);
                Store_profile_img = input.files[0];

                //alert(Store_profile_img);
                //이미지 Tag의 SRC속성에 읽어들인 File내용을 지정
                //(아래 코드에서 읽어들인 dataURL형식)
            }
            reader.readAsDataURL(input.files[0]);
            //File내용을 읽어 dataURL형식의 문자열로 저장
        }
    }//readURL()--

    //file 양식으로 이미지를 선택(값이 변경) 되었을때 처리하는 코드
    $("#FILE_TAG").change(function () {
        //선택한 이미지 경로 표시
        readURL(this);
    });
});


