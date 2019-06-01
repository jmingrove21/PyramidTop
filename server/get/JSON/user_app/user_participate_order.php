<?php
    function participate_order($json_data){
        include "../db.php";

        $order_number=$json_data['order_number'];
        $store_serial=$json_data['store_serial'];
        $user_serial=$json_data['user_serial'];
        $destination=$json_data['destination'];
        $destination_lat=$json_data['destination_lat'];
        $destination_long=$json_data['destination_long'];
        $menu=$json_data['menu'];
        date_default_timezone_set("Asia/Seoul");
        $current=date("Y-m-d H:i:s");

        $insert_query = "INSERT INTO user_order(order_number,store_serial,destination,USER_user_serial, destination_lat,destination_long,make_order_time) VALUES(".$order_number.",".$store_serial.",'".$destination."',".$user_serial.",".$destination_lat.",".$destination_long.",'".$current."')";
        $insert_stmt = mysqli_query($connect,$insert_query);

        $query = "SELECT MAX(user_order_serial) AS u FROM Capstone.user_order";
        $stmt = mysqli_query($connect,$query);
        $row = mysqli_fetch_assoc($stmt);
        $order_serial = $row['u']+1;

        foreach($menu as $m){
            $insert_query2="INSERT INTO order_menu(user_order_serial,order_number,menu_code) VALUES(".$order_serial.",".$order_number.",'".$m['menu_code']."')";
            $insert_stmt2 = mysqli_query($connect,$insert_query2);
        }

        $confirm=-1;
        if($insert_stmt & $insert_stmt2)
            $confirm=1;
        else
            $confirm=0;

        #최소주문금액 넘었는지 확인
        $query="SELECT minimum_order_price FROM store WHERE store_serial=".$store_serial;
        $stmt = mysqli_query($connect,$query);
        $row = mysqli_fetch_assoc($stmt);
        $min=$row['minimum_order_price'];

        #주문된 가격 확인
        $confirm_query="
        SELECT menu_price
        FROM Capstone.menu AS m
        INNER JOIN
        (
			SELECT menu_code
			FROM Capstone.order_menu
			WHERE order_number=".$order_number."
        ) tb
        ON m.menu_code=tb.menu_code
        WHERE store_serial=".$store_serial;

        $confirm_stmt = mysqli_query($connect,$confirm_query);
        $total_price=0;
        while($row=mysqli_fetch_assoc($confirm_stmt)){
            $total_price+=$row['menu_price'];
        }
        $condition_confirm=0;
        if($total_price>$min)
            $condition_confirm=1;


        $send_data=array(
            'confirm'=>$confirm,
            'condition_confirm'=>$condition_confirm
        );
        echo json_encode($send_data);

    }