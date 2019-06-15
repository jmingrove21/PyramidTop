// JavaScript Document

var store_serial='';
var store_name='';
var menu_img = "";



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
					/*
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
					string = string + "<input id='" + menu_code+ "_code" +"' " + "type='text' class='form-control' disabled=''>" + "</div>" + "</div>" + "</div></div></div>";

					string = string + "<div class='box-content'>" + "<div class='inner-content'>" + "<h3 class='title'>Williamson</h3>" + "<span class='post'>Web Developer</span>";
					string = string + "<ul class='icon'>" + "<li class='text-center'><a href='#'><i class='fa fa-search'></i></a></li>" + "<li><a href='#'><i class='fa fa-link'></i></a></li>" + "</ul>";
					string = string + "</div></div></div></div>";
					*/

					//alert(store_serial);
					//alert(menu_code);

					string = "<div id='" + menu_code + "' " + "class=\"col-md-3\">" + "<div class='box9'>" + "<div class='card card-user'>" + "<div class='card-header'>";
					string = string + "<div class=\"image\">" + "<img class='embed-responsive' src = \"" + menu_img + "\"" + "alt='...'></div></div>";
					//alert(string);
					//$("#menu_list_detail").append(string);
					string = string + "<div class=\"card-body\">" + "<div class='row'>" + "<div class='col-md-12 text-left'>";
					string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold;'>&nbsp;&nbsp;메뉴이름</label>";
					string = string + "<input id='"+ menu_code+ "_name" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

					string = string + "<div class='row'>" + "<div class='col-md-12 text-left'>";
					string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴가격</label>";
					string = string + "<input id='" + menu_code+ "_price" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

					string = string + "<div class='row'>" + "<div class='col-md-12 text-left'>";
					string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴코드</label>";
					string = string + "<input id='" + menu_code+ "_code" +"' " + "type='text' class='form-control' disabled=''>" + "</div></div>" + "</div></div></div>";

					string = string + "<div class='box-content'>" + "<div class='inner-content'>" + "<h3 class='title' style='font-weight: bold'>메뉴관리</h3><br>";
					string = string + "<ul class='icon'>" + "<li style='cursor:pointer'><i class='fa fa-list-alt' onclick=\"menu_modify(";
					string = string + menu_code;
					string = string + ");\""+ " data-toggle='modal' data-target='#Menu_Modal'" + "></i></li>&nbsp;&nbsp;";
					string = string + "<li style='cursor: pointer;'><i class='fa fa-trash-alt' onclick='menu_delete(" + menu_code + ");' data-toggle='modal' data-target='#Menu_delete_Modal'></i></li>" + "</ul>";
					string = string + "</div></div></div></div>";

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
						string = "<div id='" + menu_code + "' " + "class=\"col-md-3\">" + "<div class='box9'>" + "<div class='card card-user'>" + "<div class='card-header'>";
						string = string + "<div class=\"image\">" + "<img class='embed-responsive' src = \"" + menu_img + "\"" + "alt='...'></div></div>";
						//alert(string);
						//$("#menu_list_detail").append(string);
						string = string + "<div class=\"card-body\">" + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴이름</label>";
						string = string + "<input id='"+ menu_code+ "_name_in_main_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴가격</label>";
						string = string + "<input id='" + menu_code+ "_price_in_main_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴코드</label>";
						string = string + "<input id='" + menu_code+ "_code_in_main_menu_tab" +"' " + "type='text' class='form-control' disabled=''>" + "</div></div>" + "</div></div></div>";

						string = string + "<div class='box-content'>" + "<div class='inner-content'>" + "<h3 class='title' style='font-weight: bold'>메뉴관리</h3><br>";
						string = string + "<ul class='icon'>" + "<li style='cursor:pointer'><i class='fa fa-list-alt' onclick=\"menu_modify(";
						string = string + menu_code;
						string = string + ");\""+ " data-toggle='modal' data-target='#Menu_Modal'" + "></i></li>&nbsp;&nbsp;";
						string = string + "<li style='cursor:pointer'><i class='fa fa-trash-alt' onclick='menu_delete(" + menu_code + ");' data-toggle='modal' data-target='#Menu_delete_Modal'></i></li>" + "</ul>";
						string = string + "</div></div></div></div>";

						//alert(string);
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


						string = "<div id='" + menu_code + "' " + "class=\"col-md-3\">" + "<div class='box9'>" + "<div class='card card-user'>" + "<div class='card-header'>";
						string = string + "<div class=\"image\">" + "<img class='embed-responsive' src = \"" + menu_img + "\"" + "alt='...'></div></div>";
						//alert(string);
						//$("#menu_list_detail").append(string);
						string = string + "<div class=\"card-body\">" + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴이름</label>";
						string = string + "<input id='"+ menu_code+ "_name_in_side_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴가격</label>";
						string = string + "<input id='" + menu_code+ "_price_in_side_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴코드</label>";
						string = string + "<input id='" + menu_code+ "_code_in_side_menu_tab" +"' " + "type='text' class='form-control' disabled=''>" + "</div></div>" + "</div></div></div>";

						string = string + "<div class='box-content'>" + "<div class='inner-content'>" + "<h3 class='title' style='font-weight: bold'>메뉴관리</h3><br>";
						string = string + "<ul class='icon'>" + "<li style='cursor:pointer'><i class='fa fa-list-alt' onclick=\"menu_modify(";
						string = string + menu_code;
						string = string + ");\""+ " data-toggle='modal' data-target='#Menu_Modal'" + "></i></li>&nbsp;&nbsp;";
						string = string + "<li style='cursor:pointer'><i class='fa fa-trash-alt' onclick='menu_delete(" + menu_code + ");' data-toggle='modal' data-target='#Menu_delete_Modal'></i></li>" + "</ul>";
						string = string + "</div></div></div></div>";


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

						string = "<div id='" + menu_code + "' " + "class=\"col-md-3\">" + "<div class='box9'>" + "<div class='card card-user'>" + "<div class='card-header'>";
						string = string + "<div class=\"image\">" + "<img class='embed-responsive' src = \"" + menu_img + "\"" + "alt='...'></div></div>";
						//alert(string);
						//$("#menu_list_detail").append(string);
						string = string + "<div class=\"card-body\">" + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴이름</label>";
						string = string + "<input id='"+ menu_code+ "_name_in_drink_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴가격</label>";
						string = string + "<input id='" + menu_code+ "_price_in_drink_menu_tab" +"' " + "type='text' class='form-control'>" + "</div>" + "</div>" + "</div>";

						string = string + "<div class='row'>" + "<div class='col-md-12 text-left'>";
						string = string + "<div class='form-group'>" + "<label class='text-primary' style='font-weight: bold'>&nbsp;&nbsp;메뉴코드</label>";
						string = string + "<input id='" + menu_code+ "_code_in_drink_menu_tab" +"' " + "type='text' class='form-control' disabled=''>" + "</div></div>" + "</div></div></div>";

						string = string + "<div class='box-content'>" + "<div class='inner-content'>" + "<h3 class='title' style='font-weight: bold'>메뉴관리</h3><br>";
						string = string + "<ul class='icon'>" + "<li style='cursor:pointer'><i class='fa fa-list-alt' onclick=\"menu_modify(";
						string = string + menu_code;
						string = string + ");\""+ " data-toggle='modal' data-target='#Menu_Modal'" + "></i></li>&nbsp;&nbsp;";
						string = string + "<li style='cursor: pointer'><i class='fa fa-trash-alt' onclick='menu_delete(" + menu_code + ");' data-toggle='modal' data-target='#Menu_delete_Modal'></i></li>" + "</ul>";
						string = string + "</div></div></div></div>";


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

var menu_img_create = '';


function menu_create()
{
    var menu_name = "";
    var menu_price = "";
    var menu_picture = "";
    menu_name = $("#modal_create_menu_name").val();
    menu_price = $("#modal_create_menu_price").val();
	var menu_kind = "";
	menu_kind = $("#modal_create_menu_kind").val();
	if(menu_kind == "메인메뉴")
		menu_kind = "Q09A01B01";
	else if(menu_kind == "사이드메뉴")
		menu_kind = "Q09A01B02";
	else if(menu_kind == "음료")
		menu_kind = "Q09A01B03";
    var formdata = new FormData();
    formdata.append('store_info',"add_menu");
    formdata.append('menu_img', menu_img_create);
    formdata.append('store_serial', store_serial);
    formdata.append('menu_name', menu_name);
    formdata.append('menu_price', menu_price);
    formdata.append('menu_type_code', menu_kind);
	$("#Menu_create_Modal").modal('hide');
	$("#modal_create_menu_price").val("");
	$("#modal_create_menu_name").val("");
	$("#modal_create_menu_picture").attr("src","../assets/img/noimg.gif");


	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_add_menu.php",
		data: formdata,
		dataType:'text',
		encrypt:"multipart/form-data",
		processData: false,
		contentType: false,
		type: "POST",
		//contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success: function (data) {
			if (data) {
				var result3 = JSON.parse(data);
				if(result3.confirm==1)
				{
					menu_list();
					alert("해당 메뉴가 생성되었습니다!");
					location.reload();
				}
			} else {
				//alert(menu_name);
				alert("수정하기 실패");
			}
		},
		error: function (error)
		{
			alert(error);
		}

	});



}


$(document).ready(function () {

    function readURL(input) {

        if (input.files && input.files[0]) {
            var reader = new FileReader(); //파일을 읽기 위한 FileReader객체 생성
            reader.onload = function (e) {
                //파일 읽어들이기를 성공했을때 호출되는 이벤트 핸들러

                $("#modal_create_menu_picture").attr("src", e.target.result);
                //alert(e.target.result);
                //alert(input.files[0]);
                menu_img_create = input.files[0];
                //alert(Store_profile_img);
                //이미지 Tag의 SRC속성에 읽어들인 File내용을 지정
                //(아래 코드에서 읽어들인 dataURL형식)
            }
            reader.readAsDataURL(input.files[0]);

            //File내용을 읽어 dataURL형식의 문자열로 저장
        }

    }//readURL()--

    //file 양식으로 이미지를 선택(값이 변경) 되었을때 처리하는 코드
    $("#FILE_TAG_modal_createmenu").change(function () {
        //선택한 이미지 경로 표시
        readURL(this);
    });
});





function menu_delete(code) {
	var temp = code[0].innerHTML;
	var menu_code = temp.split("menu_modify(")[1].split(")")[0];
	//alert(menu_code);
	$("#modal_delete_menu_code").val(menu_code);
}


function menu_delete_submit()
{
    $("#Menu_delete_Modal").modal('hide');

	var menu_code = $("#modal_delete_menu_code").val();
	var data = {
		"store_info": "menu_delete",
		"store_serial": store_serial,
		"menu_code": menu_code
	};
	//alert(number);
	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
		type: "POST",
		data: JSON.stringify(data),
		success: function (result) {
			//alert(result);
			if (result) {
				//alert(result);
				var result1 = JSON.parse(result);
				if (result1.confirm == 1) {

					menu_list();
					alert("해당 메뉴가 삭제되었습니다!");
					location.reload();
				}

			} else
				alert("실패");
		}
	})




}
var temp_img = "";


function menu_modify_submit()
{

	var temp = menu_img.length;
	var image_check = "";
	image_check = image_check*1;
	if(temp==0)
		image_check=0;
	else
		image_check=1;
	//alert(image_check);
	var store_serial_modal = $("#modal_store_serial").val();
	var menu_code = $("#modal_menu_code").val();
	var menu_name = $("#modal_menu_name").val();
	var menu_price = $("#modal_menu_price").val();

	var formdata = new FormData();
	formdata.append('menu_img', menu_img);
	formdata.append('img_check', image_check);
	formdata.append('store_serial', store_serial_modal);
	formdata.append('menu_code', menu_code);
	formdata.append('menu_name', menu_name);
	formdata.append('menu_price', menu_price);

	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_menu_modify.php",
		data: formdata,
		dataType:'text',
		encrypt:"multipart/form-data",
		processData: false,
		contentType: false,
		type: "POST",
		//contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success: function (data) {
			if (data) {
				var result = JSON.parse(data);
				if(result.confirm==1)
				{

					$("#Menu_Modal").modal('hide');
					menu_list();
					alert("메뉴 수정 성공");
					location.reload();
				}
			} else {
				alert("메뉴 수정 실패");
			}
		},
		error: function (error)
		{
			alert(error);
		}

	});

}




function menu_modify(code)
{

	//$("#manage_menu").load("menu.html");
	var temp = code[0].innerHTML;
	var menu_code = temp.split("menu_modify(")[1].split(")")[0];
	//alert(menu_code);



	var data = {
		"store_info": "one_menu_info",
		"store_serial": store_serial,
		"menu_code": menu_code
	};
	//alert(number);
	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
		type: "POST",
		data: JSON.stringify(data),
		success: function (result) {
		    //alert(result);
			if (result)
			{
				//alert(result);
			    var result1 = JSON.parse(result);
			    $.each(result1,function(key,value)
                {
                	$("#modal_store_serial").val(store_serial);
					$("#modal_menu_name").val(value.menu_name);
					//$("#store_name1").context(decodeURIComponent(result1.store_name));
					$("#modal_menu_price").val(value.menu_price);
					$("#modal_menu_code").val(value.menu_code);
					$("#modal_menu_picture").attr('src', value.menu_img);
					temp_img = value.menu_img;



                });

			    //var result1 = JSON.parse(result);
			    //alert(result1.menu_code);
			    //alert(result1);
			}
			else
			    alert("실패");
		}
	})

}


$(document).ready(function () {

	function readURL(input) {

		if (input.files && input.files[0]) {
			var reader = new FileReader(); //파일을 읽기 위한 FileReader객체 생성
			reader.onload = function (e) {
				//파일 읽어들이기를 성공했을때 호출되는 이벤트 핸들러

				$("#modal_menu_picture").attr("src", e.target.result);
				//alert(e.target.result);
				//alert(input.files[0]);
				menu_img = input.files[0];
				//alert(Store_profile_img);
				//이미지 Tag의 SRC속성에 읽어들인 File내용을 지정
				//(아래 코드에서 읽어들인 dataURL형식)
			}
			reader.readAsDataURL(input.files[0]);

			//File내용을 읽어 dataURL형식의 문자열로 저장
		}

	}//readURL()--

	//file 양식으로 이미지를 선택(값이 변경) 되었을때 처리하는 코드
	$("#FILE_TAG_modal").change(function () {
		//선택한 이미지 경로 표시
		readURL(this);
	});
});

