<?php
    function delivery_approve($json_data){
        include '../db.php';
        date_default_timezone_set("Asia/Seoul");
        $current=date("Y-m-d H:i:s");

        $order_number=$json_data['order_number'];
        $delivery_id=$json_data['delivery_id'];
        $approve_query="UPDATE store_order SET order_status=5, delivery_approve_time='".$current."' WHERE order_number=".$order_number;
        $approve_stmt = mysqli_query($connect,$approve_query);
        

        $query="SELECT store_name,store_branch_name,store_address_jibun,store_phone,store_latitude,store_longitude FROM store WHERE store_serial=(SELECT store_serial FROM store_order WHERE order_number=".$order_number.")";
        $stmt = mysqli_query($connect,$query);
        $result=mysqli_fetch_assoc($stmt);

        $status="UPDATE delivery SET delivery_status=1,delivery_order_number=".$order_number." WHERE delivery_id='".$delivery_id."'";
        $stmt=mysqli_query($connect, $status);


        $data=array(
            'store_name'=>$result['store_name'],
            'store_branch_name'=>$result['store_branch_name'],
            'store_address'=>$result['store_address_jibun'],
            'store_phone'=>$result['store_phone'],
            'store_latitude'=>$result['store_latitude'],
            'store_longitude'=>$result['store_longitude']
        );
        echo json_encode($data,JSON_UNESCAPED_UNICODE);

    }