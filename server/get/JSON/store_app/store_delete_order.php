<?php
    function delete_order($json_data){
    include "../db.php";
    $store_serial=$json_data['store_serial'];
    $order_number=$json_data['order_number'];

    $query="UPDATE store_order SET order_status=8 WHERE order_number=".$order_number;
    $stmt = mysqli_query($connect,$query);

    $confirm=0;
    if($stmt){
            $confirm=1;
    }
    $result=array(
            'confirm'=>$confirm
        );
        echo json_encode($result,JSON_UNESCAPED_UNICODE);

    }