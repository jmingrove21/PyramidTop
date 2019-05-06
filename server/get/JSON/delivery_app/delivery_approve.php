<?php
	#post로 받아오기
    function delivery_login($json_data){
        include '../db.php';

        $id=$json_data['delivery_id'];
        $pw=$json_data['delivery_password'];
        $query = "SELECT COUNT(*) AS num FROM delivery  WHERE delivery_id='".$id."' AND delivery_password='".$pw."'";

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


