// JavaScript Document
var store_serial='';
var store_name='';
var deli_time='';
var deli_list='';


function main_list()
{
	
	 var data = {
			"store_info" : "main",
			"store_serial" : store_serial
		};
		$.ajax({
			url:"http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
			type:"POST",
			data: JSON.stringify(data),
			success: function(result) {
				if (result) {
					var result1 = JSON.parse(result);
					$.each(result1,function(key,value)
					{
						alert('key:'+key+',order_status:'+value.order_status+',order_num:'+value.order_num+', date:'+value.date+', menu:'+value.menu);
						//alert(value.menu.length);
						var count =1;
						var date = new Date(value.date);
						var year = date.getFullYear()%1000;
						var month = date.getMonth()+1;
						if(month<10)
							month = "0"+month;
						var day = date.getDate();
						var hour = date.getHours();
						var minute = date.getMinutes();
						if(minute<10)
							minute = "0" + minute;
						var order_num = value.order_num;
						var order_status = value.order_status;
						//alert(order_num);
						if(order_status==3) {
							var string_temp = "<tr id=\"";
							string_temp = string_temp + order_num + "\"" + ">";
							//alert(string_temp);
							$("#now_menu").append(string_temp); //tr 태그 추가. order_num을 id로 넘김
							//var temp = document.getElementById("now_delivery").getElementsByTagName('tr');
							//alert(temp.valueOf(id));
							$("#now_menu").append("<td style=\"text-align: left\">" + "날짜 : " + year + "." + month + "." + day + "</br>" + "시간 : " + hour + "시 " + minute + "분" + "</td>");
							//$("#now_menu").append("<td>"+"#"+value.menu[0]+"</br>"+"#"+value.menu[1]+"</br>"+"#"+value.menu[2]+"</br>"+"#"+value.menu[3]+"</td>");
							//$("#now_menu").append("<td>");
							var string = "<td>";
							for (var i = 0; i < value.menu.length; i++) {
								string = string + "#" + value.menu[i];
								if (i < value.menu.length - 1)
									string = string + "</br>";
							}
							string = string + "</td>";
							$("#now_menu").append(string);

							//여기까지 주문 메뉴 이 다음부터 아이콘

							$("#now_menu").append("<td><a class='text-info' href='#0' style='font-weight: bold'>상세보기</a></td>");
							$("#now_menu").append("<td><a class='text-info' href='#0' style='font-weight: bold'>배송출발</a></td>");
							$("#now_menu").append("</tr>");
						}
						else if(order_status==4)
						{
							var string_temp = "<tr id=\"";
							string_temp = string_temp + order_num + "\"" + ">";
							//alert(string_temp);
							$("#now_delivery").append(string_temp); //tr 태그 추가. order_num을 id로 넘김
							//var temp = document.getElementById("now_menu").getElementsByTagName('tr');
							//alert(temp.valueOf(id));
							$("#now_delivery").append("<td style=\"text-align: left\">" + "날짜 : " + year + "." + month + "." + day + "</br>" + "시간 : " + hour + "시 " + minute + "분" + "</td>");
							//$("#now_menu").append("<td>"+"#"+value.menu[0]+"</br>"+"#"+value.menu[1]+"</br>"+"#"+value.menu[2]+"</br>"+"#"+value.menu[3]+"</td>");
							//$("#now_menu").append("<td>");
							var string = "<td>";
							for (var i = 0; i < value.menu.length; i++) {
								string = string + "#" + value.menu[i];
								if (i < value.menu.length - 1)
									string = string + "</br>";
							}
							string = string + "</td>";
							$("#now_delivery").append(string);

							//여기까지 주문 메뉴 이 다음부터 아이콘

							$("#now_delivery").append("<td><a class='text-info' href='#0' style='font-weight: bold'>상세보기</a></td>");
							$("#now_delivery").append("<td><a class='text-info' href='#0' style='font-weight: bold'>배송출발</a></td>");
							$("#now_delivery").append("</tr>");
						}

					});
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
	
}
function get_main_page(){
			    document.getElementById("main_main_href_id").href = "./main.html?type="+store_name+"&"+store_serial;
}

function get_information_page(){
			    document.getElementById("main_information_href_id").href = "./information.html?type="+store_name+"&"+store_serial;
}

function get_list_page(){
			    document.getElementById("main_list_href_id").href = "./list.html?type="+store_name+"&"+store_serial;
}

function get_menu_page(){
			    document.getElementById("main_menu_href_id").href = "./menu.html?type="+store_name+"&"+store_serial;
}

function init_main_page(){
		var tmp = location.href.split("?")[1].split("=")[1];
		store_name = decodeURIComponent(tmp.split("&")[0]);
		store_serial = tmp.split("&")[1]; //시리얼 넘버
		document.getElementById("main_title_id").innerHTML=store_name;
		
		
	    get_main_page();
	    get_list_page();
		get_information_page();
	    get_menu_page();
		main_list();
	    event.preventDefault();
	
}
