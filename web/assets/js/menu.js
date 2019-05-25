// JavaScript Document

var store_serial='';
var store_name='';


function menu_list()
{
	var data = {
		"store_info" : "menu",
		"store_serial" : store_serial
	};
	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
		type: "POST",
		data: JSON.stringify(data),
		success: function (result) {
			if (result) {
				//alert(result);
				var result1 = JSON.parse(result);
				$.each(result1,function(key,value)
				{
					var menu_type_code = value.menu_type_code;
					var menu_type_name = value.menu_type_name;
					var menu_code = value.menu_code;
					var menu_name = value.menu_name;
					var menu_price = value.menu_price;
					var menu_img = value.menu_img;
					//alert(menu_price);
					var string = "";
					string = "<div id='" + menu_code + "' " + "class=\"col-md-3\">" + "<div class='card card-user'>" + "<div class='card-header'>";
					string = string + "<div class=\"image\">" + "<img class='embed-responsive' src = \"" + menu_img + "\"" + "alt='...'></div></div>";
					//alert(string);
					//$("#menu_list_detail").append(string);
					string = string + "<div class=\"card-body\">" + "<div class='row'>" + "<div class='col-md-12'>";
					string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴이름</label>";
					string = string + "<input id='"+ menu_code+ "_name" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

					string = string + "<div class='row'>" + "<div class='col-md-12'>";
					string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴가격</label>";
					string = string + "<input id='" + menu_code+ "_price" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

					string = string + "<div class='row'>" + "<div class='col-md-12'>";
					string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴코드</label>";
					string = string + "<input id='" + menu_code+ "_code" +"' " + "type='text' class='form-control' disabled=''>" + "</div>" + "</div>" + "</div>";

					$("#menu_list_entrire_detail").append(string);

					var temp = menu_code + "_name";
					document.getElementById(temp).value = menu_name;
					temp = menu_code + "_price";
					document.getElementById(temp).value = menu_price + "원";
					temp = menu_code + "_code";
					document.getElementById(temp).value = menu_code;


					//alert(string);
					if(menu_type_name == "메인메뉴")
					{
						var string = "";
						string = "<div id='" + menu_code + "' " + "class=\"col-md-3\">" + "<div class='card card-user'>" + "<div class='card-header'>";
						string = string + "<div class=\"image\">" + "<img class='embed-responsive' src = \"" + menu_img + "\"" + "alt='...'></div></div>";
						//alert(string);
						//$("#menu_list_detail").append(string);
						string = string + "<div class=\"card-body\">" + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴이름</label>";
						string = string + "<input id='"+ menu_code+ "_name_in_main_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴가격</label>";
						string = string + "<input id='" + menu_code+ "_price_in_main_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴코드</label>";
						string = string + "<input id='" + menu_code+ "_code_in_main_menu_tab" +"' " + "type='text' class='form-control' disabled=''>" + "</div>" + "</div>" + "</div>";

						$("#menu_list_main_detail").append(string);

						var temp = menu_code + "_name_in_main_menu_tab";
						document.getElementById(temp).value = menu_name;
						temp = menu_code + "_price_in_main_menu_tab";
						document.getElementById(temp).value = menu_price + "원";
						temp = menu_code + "_code_in_main_menu_tab";
						document.getElementById(temp).value = menu_code;

					}
					else if(menu_type_name == "사이드메뉴")
					{
						var string = "";
						string = "<div id='" + menu_code + "' " + "class=\"col-md-3\">" + "<div class='card card-user'>" + "<div class='card-header'>";
						string = string + "<div class=\"image\">" + "<img class='embed-responsive' src = \"" + menu_img + "\"" + "alt='...'></div></div>";
						//alert(string);
						//$("#menu_list_detail").append(string);
						string = string + "<div class=\"card-body\">" + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴이름</label>";
						string = string + "<input id='"+ menu_code+ "_name_in_side_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴가격</label>";
						string = string + "<input id='" + menu_code+ "_price_in_side_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴코드</label>";
						string = string + "<input id='" + menu_code+ "_code_in_side_menu_tab" +"' " + "type='text' class='form-control' disabled=''>" + "</div>" + "</div>" + "</div>";


						$("#menu_list_side_detail").append(string);

						var temp = menu_code + "_name_in_side_menu_tab";
						document.getElementById(temp).value = menu_name;
						temp = menu_code + "_price_in_side_menu_tab";
						document.getElementById(temp).value = menu_price + "원";
						temp = menu_code + "_code_in_side_menu_tab";
						document.getElementById(temp).value = menu_code;
					}
					else if(menu_type_name == "음료")
					{
						var string = "";
						string = "<div id='" + menu_code + "' " + "class=\"col-md-3\">" + "<div class='card card-user'>" + "<div class='card-header'>";
						string = string + "<div class=\"image\">" + "<img class='embed-responsive' src = \"" + menu_img + "\"" + "alt='...'></div></div>";
						//alert(string);
						//$("#menu_list_detail").append(string);
						string = string + "<div class=\"card-body\">" + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴이름</label>";
						string = string + "<input id='"+ menu_code+ "_name_in_drink_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴가격</label>";
						string = string + "<input id='" + menu_code+ "_price_in_drink_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>메뉴코드</label>";
						string = string + "<input id='" + menu_code+ "_code_in_drink_menu_tab" +"' " + "type='text' class='form-control' disabled=''>" + "</div>" + "</div>" + "</div>";


						$("#menu_list_drink_detail").append(string);

						var temp = menu_code + "_name_in_drink_menu_tab";
						document.getElementById(temp).value = menu_name;
						temp = menu_code + "_price_in_drink_menu_tab";
						document.getElementById(temp).value = menu_price + "원";
						temp = menu_code + "_code_in_drink_menu_tab";
						document.getElementById(temp).value = menu_code;
					}

				});

			}
			else
				alert("불러오기 실패");
		}
	});

}



function init_menu_page(){
		var tmptmp = location.href.split("html")[1];

		if(tmptmp.length==0)
		{
			alert("로그인이 필요합니다.")
			location.href = "login.html";
		}
		var tmp = location.href.split("?")[1].split("=")[1];
		store_name = decodeURIComponent(tmp.split("&")[0]);
		store_serial = tmp.split("&")[1]; //시리얼 넘버
		document.getElementById("menu_title_id").innerHTML=store_name;
		get_main_page();
	    get_list_page();
		get_information_page();
	    get_menu_page();
	    menu_list();
	    event.preventDefault();
	
}

function get_main_page(){
			    document.getElementById("menu_main_href_id").href = "./main.html?type="+store_name+"&"+store_serial;
}

function get_information_page(){
			    document.getElementById("menu_information_href_id").href = "./information.html?type="+store_name+"&"+store_serial;
}

function get_list_page(){
			    document.getElementById("menu_list_href_id").href = "./list.html?type="+store_name+"&"+store_serial;
}

function get_menu_page(){
			    document.getElementById("menu_menu_href_id").href = "./menu.html?type="+store_name+"&"+store_serial;
}