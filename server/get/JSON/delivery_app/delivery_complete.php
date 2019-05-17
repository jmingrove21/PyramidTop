<?php
    function delivery_complete($json_data){
        include "../db.php";
        $delivery_status=$json_data['delivery_status'];
        $user_serial=$json_data['user_serial'];
        $order_number=$json_data['order_number'];
        date_default_timezone_set("Asia/Seoul");
        $current=date("Y-m-d H:i:s");

        $confirm=0;
        if($delivery_status==0){
            $query="UPDATE user_order SET arrival_time='".$current."' WHERE USER_user_serial=".$user_serial;
            $stmt = mysqli_query($connect,$query);
            if($stmt)
                $confirm=1;
        }else if($delivery_status==1){
            $query1="UPDATE store_order SET order_status=7 WHERE order_number=".$order_number;
            $stmt1 = mysqli_query($connect,$query1);
            $query2="UPDATE user_order SET arrival_time='".$current."' WHERE USER_user_serial=".$user_serial;
            $stmt2 = mysqli_query($connect,$query2);
            if($stmt1&$stmt2)
                $confirm=2;
        }else{
            $confirm=0;
        }
        echo $confirm;
    }