// JavaScript Document

// JavaScript Document

var store_serial='';
var store_name='';


function init_list_page(){
		var tmp = location.href.split("?")[1].split("=")[1];
		store_name = decodeURIComponent(tmp.split("&")[0]);
		store_serial = tmp.split("&")[1]; //시리얼 넘버
		document.getElementById("list_title_id").innerHTML=store_name;
		get_main_page();
	    get_list_page();
		get_information_page();
	    get_menu_page();
		event.preventDefault();
	
}

function get_main_page(){
			    document.getElementById("list_main_href_id").href = "./main.html?type="+store_name+"&"+store_serial;
}

function get_information_page(){
			    document.getElementById("list_information_href_id").href = "./information.html?type="+store_name+"&"+store_serial;
}

function get_list_page(){
			    document.getElementById("list_list_href_id").href = "./list.html?type="+store_name+"&"+store_serial;
}

function get_menu_page(){
			    document.getElementById("list_menu_href_id").href = "./menu.html?type="+store_name+"&"+store_serial;
}

function choose_datetime()
{
	var data = {
		"store_info": "history",
		"store_serial": store_serial,
		"from_date": "2019-04-22",
		"to_date": "2019-04-24"
	};
	//alert(number);
	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
		type: "POST",
		data: JSON.stringify(data),
		success: function (result) {
			if (result) {
				alert(result);
				var result1 = JSON.parse(result);
				$.each(result1,function(key,value)
				{
					var len = value.user_order.length;
					var string = "";








				});
			}
		}
	})
}