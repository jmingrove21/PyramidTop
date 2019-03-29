<?php
    function store_login($json_data){
        include 'db.php';
        $id=$json_data['store_id'];
        $pw=$json_data['store_password'];
        $query = "SELECT COUNT(*) AS num FROM store_master_tb AS s WHERE s.store_master_id='".$id."' AND s.store_master_password='".$pw."'";
        $stmt = mysqli_query($connect,$query);
        $result = mysqli_fetch_assoc($stmt);

        $query1 = "SELECT m.store_master_num, s.store_name, s.store_serial FROM store_master_tb AS m INNER JOIN store_tb AS s ON m.store_serial=s.store_serial WHERE m.store_master_id='".$id."' AND m.store_master_password='".$pw."'";
        $stmt1 = mysqli_query($connect,$query1);
        $result1 = mysqli_fetch_assoc($stmt1);
        
        $confirm=-1;
        if($result['num']==1){
            $confirm=1; //login success
        }else{
            $confirm=0; //login fail
        }

        $send_data=array(
            'confirm'=>$confirm,
            'store_serial'=>$result1['store_serial'],
            'store_name'=>$result1['store_name']
        );
        echo json_encode($send_data);
    }
