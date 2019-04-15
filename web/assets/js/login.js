var mId = '';
var mPassword='';
var store_name = '';
var store_serial = '';


$(document).on("click", "#btn_login", function (e) {
    mId=$("#store_id").val();
    mPassword=$("#store_password").val();
    login();
});


function login()
{
	 var data = {
			 "store_info" : "login",
             "store_id" : mId,
			 "store_password" : mPassword
		};
		$.ajax({
			url:"http://54.180.102.7:80/get/JSON/store_app/store_manage.php",
			type:"POST",
			data: JSON.stringify(data),
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			success: function(result) {
				if (result) 
				{
					var result1 = JSON.parse(result);
					if(result1.confirm==1)
					{
						  store_serial=result1.store_serial;
						  store_name=result1.store_name+" "+result1.store_branch_name;
						  location.href = "main.html?type="+store_name+"&"+store_serial;
						  
						   
						   
					}
					   else if (result1.confirm==0)
						   	alert("아이디/비밀번호가 틀렸습니다");
					
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





