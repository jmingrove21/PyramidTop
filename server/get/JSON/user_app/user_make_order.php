<?php
    function user_make_order($json_data){
        include "../db.php";
        $user_serial=$json_data['user_serial'];
        $order_number=$json_data['order_number'];
        $store_serial=$json_data['store_serial'];
        $destination=$json_data['destination'];
        $destination_lat=$json_data['destination_lat'];
        $destination_long=$json_data['destination_long'];
        $menu=$json_data['menu'];
        $pay_status=$json_data['pay_status'];
        $mileage=$json_data['mileage'];
        $total_price_credit=$json_data['total_price_credit'];
        $total_price=$json_data['total_price'];

        date_default_timezone_set("Asia/Seoul");
        $current=date("Y-m-d H:i:s");

        if($order_number==0){
            $query="
            SELECT MAX(order_number) AS m FROM store_order
            ";
            $stmt = mysqli_query($connect,$query);
            $row=mysqli_fetch_assoc($stmt);
            $order_number=$row['m']+1;
            #주문 자체 생성
            $insert_query3="INSERT INTO store_order (order_number,order_status,order_create_date,store_serial) VALUES(".$order_number.",1,'".$current."',".$store_serial.")";
            $insert_stmt3=mysqli_query($connect, $insert_query3);
        }
        $get_query="SELECT user_mileage FROM Capstone.user WHERE user_serial=".$user_serial;
        $get_stmt = mysqli_query($connect,$get_query);
        $get_row=mysqli_fetch_assoc($get_stmt);
        $mil=$get_row['user_mileage']-$mileage;

        $mileage_query="UPDATE user SET user_mileage=".$mil." WHERE user_serial=".$user_serial;
        $mileage_stmt = mysqli_query($connect,$mileage_query);

        $check="SELECT COUNT(*) AS c FROM user_order WHERE order_number=".$order_number." AND USER_user_serial=".$user_serial;
        $stmt = mysqli_query($connect,$check);
        $row2=mysqli_fetch_assoc($stmt);

        $user_check=0;
        if($row2['c']!=0){
            $user_check=1;
        }else{

            #주문에 사용자에 대한 정보 추가
            $insert_query="INSERT INTO Capstone.user_order (order_number,store_serial,destination,USER_user_serial, destination_lat,destination_long,make_order_time,user_pay_status,user_pay_price,user_total_price) VALUES(".$order_number.",".$store_serial.",'".$destination."',".$user_serial.",".$destination_lat.",".$destination_long.",'".$current."',".$pay_status.",".$total_price_credit.",".$total_price.")";
            $insert_stmt = mysqli_query($connect,$insert_query);
            $query="
            SELECT MAX(user_order_serial) AS u FROM user_order
            ";
            $stmt = mysqli_query($connect,$query);
            $row=mysqli_fetch_assoc($stmt);
            $order_serial=$row['u'];

            foreach($menu as $m){
            #주문된 메뉴 추가
                $insert_query2="INSERT INTO Capstone.order_menu (user_order_serial, order_number, menu_code,menu_count) VALUES(".$order_serial.",".$order_number.",'".$m['menu_code']."',".$m['menu_count'].")";
                $insert_stmt2=mysqli_query($connect, $insert_query2);

            }

            #최소주문금액 넘었는지 확인
            $query3="SELECT minimum_order_price FROM store WHERE store_serial=".$store_serial;
            $stmt3 = mysqli_query($connect,$query3);
            $row3 = mysqli_fetch_assoc($stmt3);
            $min=$row3['minimum_order_price'];

            #주문 금액 계산
            $total=0;
            $total_query="SELECT user_total_price FROM user_order WHERE order_number=".$order_number;
            $total_stmt=mysqli_query($connect,$total_query);
            while($total_row=mysqli_fetch_assoc($total_stmt)){
                $total+=$total_row['user_total_price'];
            }

            if($total>=$min){
                $confirm=2;
                $query1="UPDATE store_order SET order_status=3, order_receipt_date='".$current."' WHERE order_number=".$order_number;
                $stmt=mysqli_query($connect,$query1);
            }else{
                $confirm=1;
            }
        }

        $send_data=array(
            'user_check'=>$user_check,
            'confirm'=>$confirm
        );
        echo json_encode($send_data);
    }