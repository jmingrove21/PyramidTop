<?php
    function delivery_approve($json_data){
        include '../db.php';
        date_default_timezone_set("Asia/Seoul");
        $current=date("Y-m-d H:i:s");

        $order_number=$json_data['order_number'];
        $store_serial=$json_data['store_serial'];
        $approve_query="UPDATE store_order SET order_status=5, delivery_approve_time='".$current."' WHERE order_number=".$order_number;
        $approve_stmt = mysqli_query($connect,$approve_query);
        

        $query="SELECT store_address_jibun,store_phone,store_latitude,store_longitude FROM store WHERE store_serial=".$store_serial;
        $stmt = mysqli_query($connect,$query);
        $result=mysqli_fetch_assoc($stmt);

        $data=array(
            'store_address'=>$result['store_address_jibun'],
            'store_phone'=>$result['store_phone'],
            'store_latitude'=>$result['store_latitude'],
            'store_longitude'=>$result['store_longitude']
        );
        echo json_encode($data,JSON_UNESCAPED_UNICODE);

    }