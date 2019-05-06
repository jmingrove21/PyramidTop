<?php
    function delivery_departure($json_data){
        include '../db.php';
        date_default_timezone_set("Asia/Seoul");
        $current=date("Y-m-d H:i:s");

        $order_number=$json_data['order_number'];
        $departure_query="UPDATE store_order SET order_status=6, delivery_approve_time='".$current."' WHERE order_number=".$order_number;
        $departure_stmt = mysqli_query($connect,$departure_query);

}