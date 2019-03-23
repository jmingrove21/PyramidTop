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


//
// $(document).on("click", "#btn_login1", function (e) {
//     //mId=$("#id").val();
//     //mPassword=$("#pw").val();
// 	store_name = $("#store_name").val(); //가게 상호명
// 	store_phone = $("#store_phone").val(); //가게 번호
// 	store_address = $("#store_address").val(); //가게 주소
// 	store_point_x = $("#store_point_x").val(); //주소의 x좌표
// 	store_point_y = $("#store_point_y").val(); //주소의 y좌표
// 	start_time = $("#start_time").val(); // 오픈시간
// 	end_time = $("#end_time").val(); //마감시간
// 	storemaster_name = $("#storemaster_name").val(); //가게주인
// 	storemaster_phone = $("#storemaster_phone").val(); //가게주인핸드폰번호
// 	storemaster_rrn = $("#storemaster_rrn").val(); //가게주 주민번호
// 	storemaster_num = $("#storemaster_num").val(); //사업자번호
//     information();
//
// });
/*
function getData3()
{
           var data1 = {
			   		"store_info" : "info",
                    "store_id" : "root", //가게 상호명
				    "store_password" : "1234" //가게 번호
           };

           var json1 = JSON.stringify(data1);
           var http1 = new XMLHttpRequest();
           var url = "http://54.180.102.7:80/get/store_manage.php";

		   http1.open('POST', encodeURI(url), true);
           http1.setRequestHeader('Content-Type', 'application/json');
           http1.onerror = function (u) {
               alert("Server not response");
           }
		   console.log(json1);
		   
	
	     
           http1.send(json1);
	
           http1.onload = function () {
		   
               if (http1.readyState == 4) 
			   {
                   if (http1.status == 200)
				   {
                       var result1 = JSON.parse(http1.responseText);
					   document.getElementById("storemaster_num").value = result1.storemaster_num;
					   document.getElementById("store_name1").value = result1.store_name;
				
                   }
                   else {
                       alert("Connect fail..");
                   }
               }
               else {
                   alert("Connect fail...");
               }
		   }
		   
		   //}
		   
		  //http1.send(json1);

}

*/

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








