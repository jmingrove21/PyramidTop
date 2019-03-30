// JavaScript Document
var store_serial='';
var store_name='';


function main_list()
{
	
	 var data = {
			"store_info" : "order",
			"store_serial" : store_serial
		};
		$.ajax({
			url:"http://54.180.102.7:80/get/store_manage.php",
			type:"POST",
			data: JSON.stringify(data),
			success: function(result) {
				if (result) {
					var result1 = JSON.parse(result);
					alert(result1);
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
function get_information_page(){
			    document.getElementById("information_href_id").href = "./information.html?type="+store_name+"&"+store_serial;
}
function init_main_page(){
		var tmp = location.href.split("?")[1].split("=")[1];
		store_name = decodeURIComponent(tmp.split("&")[0]); //가게이름
	
					store_serial = tmp.split("&")[1]; //시리얼 넘버
					document.getElementById("main_title_id").innerHTML=store_name;
	get_information_page();
	main_list();
	
}