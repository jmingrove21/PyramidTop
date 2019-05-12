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
		"from_date": "2019-02-22",
		"to_date": "2019-05-24"
	};
	//alert(number);
	$.ajax({
		url: "http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
		type: "POST",
		data: JSON.stringify(data),
		success: function (result) {
			if (result) {

				//alert(result);
				var result1 = JSON.parse(result);
				var temp_count = 1;
				temp_count = temp_count*1;

				$.each(result1,function(key,value)

				{

					var len = value.user_order.length;
					var order_num = value.order_number;
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
					var total = value.total_price;


					//alert(len);

					if(len>1)
						len = len*2-1;
					var string_temp = "<tr id=\"";
					string_temp = string_temp + order_num + "\"" + ">";
					//alert(string_temp);
					$("#past_list").append(string_temp);
					//tr 태그 추가. order_num을 id로 넘김
					string_temp = "<td rowspan=\"";

					string_temp = string_temp + len + "\" class=\"align-middle\">";
					string_temp= string_temp + year + "." + month + "." + day + "</br>" + hour + "시 " + minute + "분" + "</td>";
					//alert(string_temp);
					$("#past_list").append(string_temp);

					var temp_count = 1;
					temp_count = temp_count*1;

					//----------------여기부터는 줄을 분할해서 출력 작업중--------------------

					$.each(value.user_order, function(key,value)
					{
						var string_temp = "";
						if(temp_count>1)
						{
							string_temp = string_temp + "<tr>";
							//alert(string_temp);
							$("#past_list").append(string_temp);
						}
						string_temp = "<td class='align-middle text-success' style='font-weight: bold'>" + value.user_name + "</td>";
						//alert(string_temp);
						$("#past_list").append(string_temp);

						string_temp = "<td class='align-middle'>";
						var temp_len = value.menu.length;
						var count = 1;
						count = count*1;
						$.each(value.menu, function(key,value)
						{
							string_temp = string_temp + "#" + value.menu_name;
							if(count<temp_len)
								string_temp = string_temp + "<br>";
							count++;

						});
						string_temp = string_temp + "</td>";
						//alert(string_temp);

						$("#past_list").append(string_temp);


						//------------각 주문자의 도착시간 출력 -----------------

						string_temp = "<td class='align-middle'>";
						var date = new Date(value.arrival_time);
						var year = date.getFullYear()%1000;
						var month = date.getMonth()+1;
						if(month<10)
							month = "0"+month;
						var day = date.getDate();
						var hour = date.getHours();
						var minute = date.getMinutes();
						if(minute<10)
							minute = "0" + minute;

						string_temp= string_temp + year + "." + month + "." + day + "</br>" + hour + "시 " + minute + "분" + "</td>";
						//alert(string_temp);
						$("#past_list").append(string_temp);


						//--------------각 주문자의 주소 출력 -----------------

						var destination = value.destination;
						string_temp = "<td class='align-middle'>" + destination + "</td>"
						//alert(string_temp);
						$("#past_list").append(string_temp);

						//--------------총액 출력-------------------------
						if(temp_count==1)
						{
							string_temp = "<td rowspan=\"";
							string_temp = string_temp + len + "\" class='align-middle text-primary' style='font-weight:bold'>";

							string_temp= string_temp + total + "원" + "</td>";
							//alert(string_temp);
							$("#past_list").append(string_temp);
						}
						//alert("</tr>");
						$("#past_list").append("</tr>")
						temp_count = temp_count+1;


					});

					//alert(len);


				});

			}
		}
	})
}