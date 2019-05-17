// JavaScript Document

var store_serial='';
var store_name='';


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