<?php
    function store_login($json_data){
        include 'db.php';
        $id=$json_data['store_id'];
        $pw=$json_data['store_password'];
        $query = "SELECT COUNT(*) AS num FROM STORE AS s WHERE s.store_id='".$id."' AND s.store_pw='".$pw."'";
        $stmt = mysqli_query($connect,$query);
        $result = mysqli_fetch_assoc($stmt);

        $confirm=-1;
        if($result['num']==1){
            $confirm=1; //login success
        }else{
            $confirm=0; //login fail
        }

        $send_data=array(
            'confirm'=>$confirm
        );
        echo json_encode($send_data);
    }
