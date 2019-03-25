<?php
    function store_info($json_data){
        include 'db.php';
        header('Content-Type: text/html; charset=utf-8');
        $id=$json_data['store_id'];
        $pw=$json_data['store_password'];
        $query = "SELECT storemaster_num, store_name, storemaster_name, store_address, start_time, end_time, store_restday, store_notice, store_phone, store_profile_img FROM STORE AS s WHERE s.store_id='".$id."' AND s.store_pw='".$pw."'";
        $stmt = mysqli_query($connect,$query);
        $result = mysqli_fetch_assoc($stmt);
        $send_data=array(
            'storemaster_num'=>$result['storemaster_num'],
            'store_name'=>$result['store_name'],
            'storemaster_name'=>$result['storemaster_name'],
            'store_address'=>$result['store_address'],
            'start_time'=>$result['start_time'],
            'end_time'=>$result['end_time'],
            'store_restday'=>$result['store_restday'],
            'store_notice'=>$result['store_notice'],
            'store_phone'=>$result['store_phone'],
            'store_profile_img'=>$result['store_profile_img']
        );
        echo json_encode($send_data);
    }