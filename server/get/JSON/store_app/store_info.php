<?php
    function store_info($json_data){
        include '../db.php';
        header('Content-Type: text/html; charset=utf-8');
        $store_serial=$json_data['store_serial'];
        $query = "SELECT m.store_master_num, s.store_name, m.store_master_name, s.store_address, s.start_time, s.end_time, s.store_restday, s.store_notice, m.store_master_phone, s.store_profile_img FROM store AS s INNER JOIN store_master AS m ON s.store_serial=m.store_serial WHERE s.store_serial='".$store_serial."'";
        $stmt = mysqli_query($connect,$query);
        $result = mysqli_fetch_assoc($stmt);
        $send_data=array(
            'storemaster_num'=>$result['store_master_num'],
            'store_name'=>$result['store_name'],
            'storemaster_name'=>$result['store_master_name'],
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