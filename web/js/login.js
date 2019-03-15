var mId = '';
var mPassword='';

$(document).on("click", "#btn_login", function (e) {
    mId=$("#id").val();
    mPassword=$("#pw").val();
    login();
});


function login() 
{
           var data = {
               "store_id" : mId,
			   "store_password" : mPassword
           };

           var json = JSON.stringify(data);
           var http = new XMLHttpRequest();
           var url = "http://54.180.102.7:80/get/JSON/login.php";

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
						   location.href = "Main.html";
					   		//alert("success");
						    
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
		   
}