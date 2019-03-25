var mId = '';
var mPassword='';
var storemaster_num = '';
var store_name = '';

$(document).on("click", "#btn_login", function (e) {
    mId=$("#store_id").val();
    mPassword=$("#store_password").val();
    login();
});

/*
function login() 
{
           var data = {
			   "store_info" : "login",
               "store_id" : mId,
			   "store_password" : mPassword
           };

           var json = JSON.stringify(data);
           var http = new XMLHttpRequest();
           var url = "http://54.180.102.7:80/get/store_manage.php";

           http.open('POST', url, true);
           http.setRequestHeader('Content-type', 'application/json');
           http.onerror = function (e) {
               alert("Server not response");
           }
           http.send(json);
           http.onload = function () {
               if (http.readyState == 4) {
                   if (http.status == 200) 
				   {
                       var result = JSON.parse(http.responseText);
					   
				       if(result.confirm==1)
					   {
						   location.href = "main.html";
						  document.getElementById("store_name").val
						  document.getElementById("storemaster_num").value = result.storemaster_num;
						  alert(result.storemaster_num);
						   
						   
					   }
					   else if (result.confirm==0)
						   	alert("아이디/비밀번호가 틀렸습니다");
					   
					   

                   }
                   else {
                       alert("Connect fail..");
                   }
               }
               else {
                   alert("Connect fail...");
               }
		   }
		   //http.send(json);
		   
}

*/

function login()
{
	 var data = {
			 "store_info" : "login",
               "store_id" : mId,
			   "store_password" : mPassword
		};
		$.ajax({
			url:"http://54.180.102.7:80/get/store_manage.php",
			type:"POST",
			data: JSON.stringify(data),
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			success: function(result) {
				if (result) 
				{
					var result1 = JSON.parse(result);
					if(result1.confirm==1)
					{
						
						  location.href = "main.html";
						  $("#store_name").val(result1.store_name);
						  $("#storemaster_num").val(result1.storemaster_num);
						  alert(result1.storemaster_num);
						   
						   
					}
					   else if (result1.confirm==0)
						   	alert("아이디/비밀번호가 틀렸습니다");
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
	//event.preventDefault();
	
}





