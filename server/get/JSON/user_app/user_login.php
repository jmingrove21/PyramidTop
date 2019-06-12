<?php
	#post로 받아오기
    function user_login($json_data){
        include '../db.php';

        $id=$json_data['user_id'];
        $pw=$json_data['user_password'];
        $query = "SELECT COUNT(*) AS num FROM user AS u WHERE u.user_id='".$id."' AND u.user_pw='".$pw."'";
        $stmt = mysqli_query($connect,$query);
        $result = mysqli_fetch_assoc($stmt);


        $query1 = "SELECT u.user_serial,u.user_name,u.user_mileage FROM user AS u WHERE u.user_id='".$id."' AND u.user_pw='".$pw."'";
        $stmt1 = mysqli_query($connect,$query1);
        $result1 = mysqli_fetch_assoc($stmt1);

        $confirm=-1;
        if($result['num']=="1"){
            $confirm=1; //login success
        }else{
            $confirm=0; //login fail
        }

        $send_data=array(
            'confirm'=>$confirm,
            'user_serial'=>$result1['user_serial'],
            'user_name'=>$result1['user_name'],
            'user_mileage'=>$result1['user_mileage']
        );

        echo json_encode($send_data,JSON_UNESCAPED_UNICODE);
	}


