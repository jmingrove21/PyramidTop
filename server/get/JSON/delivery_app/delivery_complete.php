<?php
    function delivery_complete($json_data){
        include "../db.php";
        $delivery_status=$json_data['delivery_status'];
        $delivery_id=$json_data['delivery_id'];
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
        }
        else if($delivery_status==1){
            $query1="UPDATE store_order SET order_status=7 WHERE order_number=".$order_number;
            $stmt1 = mysqli_query($connect,$query1);
            $query2="UPDATE user_order SET arrival_time='".$current."' WHERE USER_user_serial=".$user_serial;
            $stmt2 = mysqli_query($connect,$query2);
            if($stmt1&&$stmt2){
                $confirm=2;
            }
            ##마일리지 처리
            $count_query="SELECT COUNT(*) AS c,store_serial FROM user_order WHERE order_number=".$order_number." GROUP BY store_serial";
            $stmt_count = mysqli_query($connect,$count_query);
            $count_row=mysqli_fetch_assoc($stmt_count);
            $count=$count_row['c'];


            $cost_query="SELECT delivery_cost FROM store WHERE store_serial=".$count_row['store_serial'];
            $stmt_cost = mysqli_query($connect,$cost_query);
            $cost_row=mysqli_fetch_assoc($stmt_cost);
            $cost=$cost_row['delivery_cost'];

            $percent=1;
            if($count==1){
                $percent=1;
            }else if($count==2){
                $percent=0.1;
            }else if($count==3){
                $percent=0.2;
            }else{
                $percent=0.25;
            }

            $mileage=$cost*$percent;

            $user_query="SELECT USER_user_serial FROM user_order WHERE order_number=".$order_number;
            $user_count = mysqli_query($connect,$user_query);
            while($user_row=mysqli_fetch_assoc($user_count)){
                $mileage_query="UPDATE user SET user_mileage=".$mileage." WHERE user_serial=".$user_row['USER_user_serial'];
                $mileage_stmt = mysqli_query($connect,$mileage_query);
            }



        }
        else{
            $confirm=0;
        }
        $status="UPDATE delivery SET delivery_status=0,delivery_order_number=0 WHERE delivery_id='".$delivery_id."'";
        $stmt=mysqli_query($connect, $status);

        echo $confirm;
    }