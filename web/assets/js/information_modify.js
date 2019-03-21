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


$(document).on("click", "#btn_modify", function (a) {
    mId2=$("#store_id").val();
    mPassword2=$("#store_pw").val();
  	Store_name = $("#store_name").val(); //가게 상호명
  	Store_phone = $("#store_phone").val(); //가게 번호
  	Store_address = $("#store_address").val(); //가게 주소
  	Start_time = $("#start_time").val(); // 오픈시간
  	End_time = $("#end_time").val(); //마감시간
  	Storemaster_name = $("#storemaster_name").val(); //가게주인
  	Storemaster_phone = $("#storemaster_phone").val(); //가게주인핸드폰번호
  	Storemaster_rrn = $("#storemaster_rrn").val(); //가게주 주민번호
  	Storemaster_num = $("#storemaster_num").val(); //사업자번호
	Store_notice = $("#store_notice").val();
	Store_restday = $("#store_restday").val();
    modifyData();

});

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

/*

function getData3()
{
	 var data = {
			"store_info" : 'info',
			"store_id" : 'root',
			"store_password" : '1234'
		};
		$.ajax({
			url:"http://54.180.102.7:80/get/store_manage.php",
			type:"POST",
			data: JSON.stringify(data),
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			success: function(result) {
				if (result) {
					var result1 = JSON.parse(result);
					$("#store_name").val(decodeURIComponent(result1.store_name));
					$("#store_name1").val(decodeURIComponent(result1.store_name));
					$("#store_address").val(result1.store_address);
					$("#storemaster_name").val(result1.storemaster_name);
					$("#storemaster_num").val(result1.storemaster_num);
					$("#start_time").val(result1.start_time);
					$("#end_time").val(result1.end_time);
					$("#store_restday").val(result1.store_restday);
					$("#start_time").val(result1.start_time);
					$("#end_time").val(result1.end_time);
					$("#store_name").val(result1.store_name);
					$("#store_notice").val(result1.store_notice);
					
					$("#store_phone").val(result1.store_phone);
					//document.getElementById("storemaster_num").value=result.storemaster_num;
					//document.getElementById("store_name1").value=result.store_name;
					//alert(result);

				} else {
					alert("불러오기 실패");
				}
			},
			error: function(error){
					alert(error);
			}
			
		});
	event.preventDefault();
	
}








