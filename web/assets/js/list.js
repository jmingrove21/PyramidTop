// JavaScript Document

// JavaScript Document

var store_serial='';
var store_name='';


function init_list_page(){
		var tmptmp = location.href.split("html")[1];

		if(tmptmp.length==0)
		{
			alert("로그인이 필요합니다.")
			location.href = "login.html";
		}
		var tmp = location.href.split("?")[1].split("=")[1];
		store_name = decodeURIComponent(tmp.split("&")[0]);
		store_serial = tmp.split("&")[1]; //시리얼 넘버
		document.getElementById("list_title_id").innerHTML=store_name;
		//var today = new Date();


		var today = new Date();
		var year = today.getFullYear();
		var month = today.getMonth()+1;
		if(month<10)
			month = "0"+month;
		var day = today.getDate();
		if(day<10)
			day = "0"+day;
		var hour = today.getHours();
		var minute = today.getMinutes();
		if(minute<10)
			minute = "0" + minute;

		var temp = "";
		if(hour>=12)
		{
			temp = "pm"
			if(hour=="13")
				hour = "01";
			else if(hour=="14")
				hour = "02";
			else if(hour=="15")
				hour = "03";
			else if(hour=="16")
				hour = "04";
			else if(hour=="17")
				hour = "05";
			else if(hour=="18")
				hour = "06";
			else if(hour=="19")
				hour = "07";
			else if(hour=="20")
				hour = "08";
			else if(hour=="21")
				hour = "09";
			else if(hour=="22")
				hour = "10";
			else if(hour=="23")
				hour = "11";
		}
		else
			temp = "am";

		var end;
		end = year + "-" + month + "-" + day + " " + hour + ":" + minute + " " +temp;

		//alert(start);

		var before = new Date();
		var week = before.getTime()-(7*24*60*60*1000);
		before.setTime(week);

		var year = before.getFullYear();
		var month = before.getMonth()+1;
		if(month<10)
			month = "0"+month;
		var day = before.getDate();
		if(day<10)
			day = "0"+day;
		var hour = before.getHours();
		var minute = before.getMinutes();
		if(minute<10)
			minute = "0" + minute;

		var temp = "";
		if(hour>=12)
		{
			temp = "pm"
			if(hour=="13")
				hour = "01";
			else if(hour=="14")
				hour = "02";
			else if(hour=="15")
				hour = "03";
			else if(hour=="16")
				hour = "04";
			else if(hour=="17")
				hour = "05";
			else if(hour=="18")
				hour = "06";
			else if(hour=="19")
				hour = "07";
			else if(hour=="20")
				hour = "08";
			else if(hour=="21")
				hour = "09";
			else if(hour=="22")
				hour = "10";
			else if(hour=="23")
				hour = "11";
		}
		else
			temp = "am";

		var start;
		start = year + "-" + month + "-" + day + " " + hour + ":" + minute + " " +temp;

		//alert(end);


		$("#pick_start").val(start);
		$("#pick_end").val(end);
		get_main_page();
	    get_list_page();
		get_information_page();
	    get_menu_page();
	    choose_datetime();
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

	var start_date = $("#pick_start").val();
	//alert(start_date);
	var end_date = $("#pick_end").val();
	//alert(end_date);
	var from_date = start_date.split(" ")[0];
	//alert(from_date);
	var to_date = end_date.split(" ")[0];
	//alert(to_date);

	var from_time = start_date.split(" ")[1];
	//alert(from_time);
	var end_time = end_date.split(" ")[1];
	//alert(end_time);

	var from_ampm = start_date.split(" ")[2];
	var end_ampm = end_date.split(" ")[2];

	//alert(from_ampm);
	//alert(end_ampm);

	if(from_ampm=="pm")
	{
		var temp = from_time.split(":")[0];
		if(temp=="01")
			temp = "13";
		else if(temp=="02")
			temp = "14";
		else if(temp=="03")
			temp = "15";
		else if(temp=="04")
			temp = "16";
		else if(temp=="05")
			temp = "17";
		else if(temp=="06")
			temp = "18";
		else if(temp=="07")
			temp = "19";
		else if(temp=="08")
			temp = "20";
		else if(temp=="09")
			temp = "21";
		else if(temp=="10")
			temp = "22";
		else if(temp=="11")
			temp = "23";

		from_time = temp + ":" + from_time.split(":")[1];
		//alert(from_time);

	}

	if(from_ampm=="am")
	{
		var temp = from_time.split(":")[0];
		if(temp=="12")
			temp = "00";
		from_time = temp + ":" + from_time.split(":")[1];
	}

	from_time = from_time + ":" + "00";

	//alert(from_time);

	if(end_ampm=="pm")
	{
		var temp = end_time.split(":")[0];
		if(temp=="01")
			temp = "13";
		else if(temp=="02")
			temp = "14";
		else if(temp=="03")
			temp = "15";
		else if(temp=="04")
			temp = "16";
		else if(temp=="05")
			temp = "17";
		else if(temp=="06")
			temp = "18";
		else if(temp=="07")
			temp = "19";
		else if(temp=="08")
			temp = "20";
		else if(temp=="09")
			temp = "21";
		else if(temp=="10")
			temp = "22";
		else if(temp=="11")
			temp = "23";

		end_time = temp + ":" + end_time.split(":")[1];
		//alert(end_time);

	}

	if(end_ampm=="am")
	{
		var temp = end_time.split(":")[0];
		if(temp=="12")
			temp = "00";
		end_time = temp + ":" + end_time.split(":")[1];
	}

	end_time = end_time + ":" + "00";

	//alert(end_time);

	from_date = from_date + " " + from_time;
	to_date = to_date + " " + end_time;
	//alert(from_date);
	//alert(to_date);

	var data = {
		"store_info": "history",
		"store_serial": store_serial,
		"from_date": from_date,
		"to_date": to_date
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
				$("#past_list").empty();
				var temp = result.length;
				//alert(temp);
				if(temp==23)
					$("#past_list").append("<tr><td colspan='6' class='text-center text-gray'  style='font-size: x-large; font-weight: bold'><br>주문이 존재하지 않습니다</td></tr>");
				else {
					$.each(result1, function (key, value) {

						var len = value.user_order.length;
						var order_num = value.order_number;
						var date = new Date(value.order_receipt_date);
						var year = date.getFullYear() % 1000;
						var month = date.getMonth() + 1;
						if (month < 10)
							month = "0" + month;
						var day = date.getDate();
						var hour = date.getHours();
						var minute = date.getMinutes();
						if (minute < 10)
							minute = "0" + minute;
						var total = value.total_price;


						//alert(len);

						if (len > 1)
							len = len * 2 - 1;
						var string_temp = "<tr id=\"";
						string_temp = string_temp + order_num + "\"" + ">";
						//alert(string_temp);
						$("#past_list").append(string_temp);
						//tr 태그 추가. order_num을 id로 넘김
						string_temp = "<td rowspan=\"";

						string_temp = string_temp + len + "\" class=\"align-middle\">";
						string_temp = string_temp + year + "." + month + "." + day + "</br>" + hour + "시 " + minute + "분" + "</td>";
						//alert(string_temp);
						$("#past_list").append(string_temp);

						var temp_count = 1;
						temp_count = temp_count * 1;

						//----------------여기부터는 줄을 분할해서 출력 작업중--------------------

						$.each(value.user_order, function (key, value) {
							var string_temp = "";
							if (temp_count > 1) {
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
							count = count * 1;
							$.each(value.menu, function (key, value) {
								string_temp = string_temp + "#" + value.menu_name;
								if (count < temp_len)
									string_temp = string_temp + "<br>";
								count++;

							});
							string_temp = string_temp + "</td>";
							//alert(string_temp);

							$("#past_list").append(string_temp);


							//------------각 주문자의 도착시간 출력 -----------------

							string_temp = "<td class='align-middle'>";
							var date = new Date(value.arrival_time);
							var year = date.getFullYear() % 1000;
							var month = date.getMonth() + 1;
							if (month < 10)
								month = "0" + month;
							var day = date.getDate();
							var hour = date.getHours();
							var minute = date.getMinutes();
							if (minute < 10)
								minute = "0" + minute;

							string_temp = string_temp + year + "." + month + "." + day + "</br>" + hour + "시 " + minute + "분" + "</td>";
							//alert(string_temp);
							$("#past_list").append(string_temp);


							//--------------각 주문자의 주소 출력 -----------------

							var destination = value.destination;
							string_temp = "<td class='align-middle'>" + destination + "</td>"
							//alert(string_temp);
							$("#past_list").append(string_temp);

							//--------------총액 출력-------------------------
							if (temp_count == 1) {
								string_temp = "<td rowspan=\"";
								string_temp = string_temp + len + "\" class='align-middle text-primary' style='font-weight:bold'>";

								string_temp = string_temp + total + "원" + "</td>";
								//alert(string_temp);
								$("#past_list").append(string_temp);
							}
							//alert("</tr>");
							$("#past_list").append("</tr>")
							temp_count = temp_count + 1;


						});

						//alert(len);


					});
				}

			}
		}
	})
}