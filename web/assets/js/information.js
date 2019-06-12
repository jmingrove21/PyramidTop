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
var store_profile_img = '';
var store_serial = '';
var store_name_title = '';
var store_minimum_order_price = '';



function init_information_page()
{
	var tmptmp = location.href.split("html")[1];

	if(tmptmp.length==0)
	{
		alert("로그인이 필요합니다.")
		location.href = "login.html";
	}
	var tmp = location.href.split("?")[1].split("=")[1];
	store_name_title = decodeURIComponent(tmp.split("&")[0]);
	store_serial = tmp.split("&")[1]; //시리얼 넘버
	document.getElementById("information_title_id").innerHTML=store_name_title;
	get_main_page();
	get_list_page();
	get_information_page();
	get_menu_page();
	var data = {
			"store_info" : 'info',
			"store_serial" : store_serial
		};
		$.ajax({
			url:"http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
			type:"POST",
			data: JSON.stringify(data),
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			success: function(result) {
				if (result) {
					var result1 = JSON.parse(result);
					$("#store_name").val(decodeURIComponent(result1.store_name));
					//$("#store_name1").context(decodeURIComponent(result1.store_name));
					$("#store_address").val(result1.store_address);
					$("#storemaster_name").val(result1.storemaster_name);
					$("#storemaster_num").val(result1.storemaster_num);
					$("#cost_lower_bound").val(result1.minimum_order_price);
					$("#start_time").val(result1.start_time);
					$("#end_time").val(result1.end_time);
					$("#store_restday").val(result1.store_restday);
					$("#start_work_time").val(result1.start_time);
					$("#end_work_time").val(result1.end_time);
					$("#store_name").val(result1.store_name);
					$("#store_notice").val(result1.store_notice);
					$("#store_phone").val(result1.store_phone);
					$("#delivery_cost").val(result1.delivery_cost);
					$("#store_profile").attr('src', result1.store_profile_img);

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


function get_main_page(){
			    document.getElementById("information_main_href_id").href = "./main.html?type="+store_name_title+"&"+store_serial;
}

function get_information_page(){
			    document.getElementById("information_information_href_id").href = "./information.html?type="+store_name_title+"&"+store_serial;
}

function get_list_page(){
			    document.getElementById("information_list_href_id").href = "./list.html?type="+store_name_title+"&"+store_serial;
}

function get_menu_page(){
			    document.getElementById("information_menu_href_id").href = "./menu.html?type="+store_name_title+"&"+store_serial;
}








