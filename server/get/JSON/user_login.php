<?php
	#post로 받아오기
    function user_login($json_data){
        include 'db.php';

        $id=$json_data['user_id'];
        $pw=$json_data['user_password'];
        $query = "SELECT COUNT(*) AS num FROM USER AS u WHERE u.user_id='".$id."' AND u.user_pw='".$pw."'";
        $stmt = mysqli_query($connect,$query);
        $result = mysqli_fetch_assoc($stmt);

        $confirm=-1;
        if($result['num']=="1"){
            $confirm=1; //login success
        }else{
            $confirm=0; //login fail
        }

        $send_data=array(
            'confirm'=>$confirm
        );

        echo json_encode($send_data);
	}


