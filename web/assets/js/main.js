// JavaScript Document
var store_serial='';
var store_name='';
var deli_time='';
var deli_list='';

function logout()
{
	location.replace("login.html");
}



function main_list()
{
	var count_cooking =0;
	var count_delivery=0;
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
					$("#now_menu").empty();
					$("#now_delivery").empty();
					var result1 = JSON.parse(result);
					$.each(result1,function(key,value)
					{
						//alert('key:'+key+',order_status:'+value.order_status+',order_num:'+value.order_num+', date:'+value.date+', menu:'+value.menu);
						//alert(value.menu.length);

						var order_num = value.order_num;
						var order_status = value.order_status;
						//alert(order_num);
						if(order_status==3) {
							count_cooking++;
							var date = new Date(value.order_receipt_date);
							var year = date.getFullYear()%1000;
							var month = date.getMonth()+1;
							if(month<10)
								month = "0"+month;
							var day = date.getDate();
							var hour = date.getHours();
							var minute = date.getMinutes();
							if(minute<10)
								minute = "0" + minute;
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
							string = "<td class='text-center'><button type='button' class='btn btn-primary btn-sm' onclick='detail_deliver_list(";
							string = string + order_num;
							string = string + ");' data-toggle='modal' data-target='#myModal'>상세보기</button></td>";
							//alert(string);
							$("#now_menu").append(string);
							//$("#now_menu").append("<td class='text-center'><button type='button' class='btn btn-primary btn-sm' data-toggle='modal' data-target='#myModal' onclick='detail_deliver_list();'>상세보기</button></td>");
							string = "<td class='text-center'><button type='button' class='btn btn-primary btn-sm' onclick='change_deli_status(";
							string = string + order_num;
							string = string + ");'>출발</button></td>";
							string = string + "<td class='text-center'><button type='button' class='btn btn-danger btn-sm' onclick='delete_delivery(";
							string = string + order_num;
							string = string + ");'>X</button></td>";
							//alert(string);
							//$("#now_menu").append(string);

							//alert(string);
							$("#now_menu").append(string);
							//$("#now_menu").append("<td class='text-center for_change_status'><button type='button' class='btn btn-primary btn-sm' onclick='change_deli_status();'>배송출발</button></td>");
							$("#now_menu").append("</tr>");
						}
						else if(order_status==4)
						{
							count_delivery++;
							var string_temp = "<tr id=\"";
							string_temp = string_temp + order_num + "\"" + ">";
							//alert(string_temp);
							$("#now_delivery").append(string_temp); //tr 태그 추가. order_num을 id로 넘김
							//var temp = document.getElementById("now_menu").getElementsByTagName('tr');
							//alert(temp.valueOf(id));
							$("#now_delivery").append("<td style='font-weight: bold' style='text-align: left' class='text-success'>" + "대기중" + "</td>");
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
							string = "<td class='text-center'><button type='button' class='btn btn-primary btn-sm' onclick='detail_deliver_list(";
							string = string + order_num;
							string = string + ");' data-toggle='modal' data-target='#myModal'>상세보기</button></td>";
							//alert(string);
							$("#now_delivery").append(string);
							if(order_status==4)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-light btn-sm'>배달대기 </button></td>");
							else if(order_status==5)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-warning btn-sm'>배달승인 </button></td>");
							else if(order_status==6)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-success btn-sm'>배달중 </button></td>");

							$("#now_delivery").append("</tr>");
						}

						else if(order_status==5)
						{
							count_delivery++;
							var date = new Date(value.delivery_approve_time);
							var year = date.getFullYear()%1000;
							var month = date.getMonth()+1;
							if(month<10)
								month = "0"+month;
							var day = date.getDate();
							if(day<10)
								day = "0" + day;
							var hour = date.getHours();
							var minute = date.getMinutes();
							if(minute<10)
								minute = "0" + minute;
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
							string = "<td class='text-center'><button type='button' class='btn btn-primary btn-sm' onclick='detail_deliver_list(";
							string = string + order_num;
							string = string + ");' data-toggle='modal' data-target='#myModal'>상세보기</button></td>";
							//alert(string);
							$("#now_delivery").append(string);
							if(order_status==4)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-light btn-sm'>배달대기 </button></td>");
							else if(order_status==5)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-warning btn-sm'>배달승인 </button></td>");
							else if(order_status==6)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-success btn-sm'>배달중 </button></td>");

							$("#now_delivery").append("</tr>");
						}


						else if(order_status==6)
						{
							count_delivery++;
							var date = new Date(value.delivery_departure_date);
							var year = date.getFullYear()%1000;
							var month = date.getMonth()+1;
							if(month<10)
								month = "0"+month;
							var day = date.getDate();
							var hour = date.getHours();
							var minute = date.getMinutes();
							if(minute<10)
								minute = "0" + minute;
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
							string = "<td class='text-center'><button type='button' class='btn btn-primary btn-sm' onclick='detail_deliver_list(";
							string = string + order_num;
							string = string + ");' data-toggle='modal' data-target='#myModal'>상세보기</button></td>";
							//alert(string);
							$("#now_delivery").append(string);
							if(order_status==4)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-light btn-sm'>배달대기 </button></td>");
							else if(order_status==5)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-warning btn-sm'>배달승인 </button></td>");
							else if(order_status==6)
								$("#now_delivery").append("<td class='text-center'><button id='status_delivery' type='button' class='btn btn-success btn-sm'>배달중 </button></td>");

							$("#now_delivery").append("</tr>");
						}


					});

					if(count_cooking==0)
						$("#now_menu").append("<tr><td colspan='5' class='text-gray text-center' style='font-weight: bold; font-size: x-large'><br>주문이 존재하지 않습니다</td></tr>");
					if(count_delivery==0)
						$("#now_delivery").append("<tr><td colspan='4' class='text-gray text-center' style='font-weight: bold; font-size: x-large'><br>주문이 존재하지 않습니다</td></tr>")

					var number_cooking="";
					number_cooking = count_cooking + "건";
					var number_delivery="";
					number_delivery = count_delivery + "건";
					document.getElementById("number_cooking").innerHTML = number_cooking;
					document.getElementById("number_delivery").innerHTML = number_delivery;
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
		var tmptmp = location.href.split("html")[1];

		if(tmptmp.length==0)
		{
			alert("로그인이 필요합니다.")
			location.href = "login.html";
		}

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

function delete_delivery(number)
{
	var data = {
		"store_info" : "delete_order",
		"store_serial" : store_serial,
		"order_number" : number
	};

	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
		type: "POST",
		data: JSON.stringify(data),
		success: function (result) {
			if (result) {
				var result1 = JSON.parse(result);
				//alert(result1.confirm);
				if (result1.confirm == 1) {
					alert("해당 주문을 취소했습니다.");
					location.reload();
				}
				else
					alert("실패");
			}
		}
	})
}



function change_deli_status(number) {
	var data = {
		"store_info": "complete_order",
		"store_serial": store_serial,
		"order_number": number
	};
	//alert(number);
	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
		type: "POST",
		data: JSON.stringify(data),
		success: function (result) {
			if (result) {
				var result1 = JSON.parse(result);
				//alert(result1.confirm);
				if (result1.confirm == 1) {
					alert("해당 주문의 배달 요청이 접수 되었습니다.");
					location.reload();
				}
				else
					alert("실패");
			}
		}
	})
}

function detail_deliver_list(number)
{

	var data = {
		"store_info": "detail_main",
		"store_serial": store_serial,
		"order_number": number
	};
	//alert(number);
	$.ajax
	({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
		type: "POST",
		data: JSON.stringify(data),
		success: function (result)
		{
			//alert(result);
			var result1 = JSON.parse(result)
			if(result1)
			{
				$("#myModal_text").empty();
				var total=0;
				total = total*1;
				$.each(result1,function(key,value) {
					//alert(value.user_serial);
					var temp_string="";
					var subtotal = 0;
					subtotal = subtotal*1;
					//----------------------------아이디를 내용에 넣음----------------------------
					var user_id = value.user_id;
					temp_string = "<h6 class='text-success' style='font-weight: bold'>"+ user_id + "</h6>"+ "<br>";
					$("#myModal_text").append(temp_string);

					//---------------------------번호를 내용에 넣음---------------------------

					var user_phone = value.user_phone;
					temp_string = "전화번호"+"&nbsp;&nbsp;:&nbsp;&nbsp;" + user_phone + "<br>";
					$("#myModal_text").append(temp_string);

					//--------------------------주소를 내용에 넣음---------------------------

					var destination = value.destination;
					temp_string = "주문주소"+"&nbsp;&nbsp;:&nbsp;&nbsp;" + destination + "<br>";
					$("#myModal_text").append(temp_string);

					//--------------------------메뉴를 내용에 넣음---------------------------
					temp_string="주문메뉴" + "&nbsp;&nbsp;:&nbsp;&nbsp;";
					var temp_string2 = "";
					$.each(value.user_menu ,function(key,value)
					{
						var price = value.menu_price*1;
						var menu_count = value.menu_count*1;
						if(menu_count>1)
							temp_string2 = temp_string2 + value.menu_name + "*" + value.menu_count + " , "
						else
							temp_string2 = temp_string2 + value.menu_name + " , ";

						subtotal = subtotal + price*menu_count;

					});
					temp_string2 = temp_string2.substr(0,temp_string2.length-2);
					temp_string = temp_string + temp_string2 + "<br>";
					$("#myModal_text").append(temp_string);
					//--------------------------주문 들어온 날짜 및 시간을 내용에 넣음----------------

					var date = new Date(value.order_receipt_date);
					var year = date.getFullYear()%1000;
					var month = date.getMonth()+1;
					if(month<10)
						month = "0"+month;
					var day = date.getDate();
					var hour = date.getHours();
					var minute = date.getMinutes();
					if(minute<10)
						minute = "0" + minute;
					temp_string = "주문날짜"+"&nbsp;&nbsp;:&nbsp;&nbsp;" + year+ "년 " + month +"월 "+ day +"일 " + "<br>";
					$("#myModal_text").append(temp_string);

					temp_string = "주문시간" + "&nbsp;&nbsp;:&nbsp;&nbsp;" + hour +"시"+ minute + "분" + "<br>" + "<br>"
					$("#myModal_text").append(temp_string);

					$("#myModal_text").append("<h6 class='pull-right text-primary' style='font-weight: bold'> 금액 : "+ subtotal + "원" +"</h6>");

					$("#myModal_text").append("<br><hr>");

					total = total + subtotal;

				});

				$("#myModal_text").append("<h5 class='pull-right text-primary' style='font-weight: bold'> 총금액 : " + total + "원" +"</h5>");



			}
		}

	});
}