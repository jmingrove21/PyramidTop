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

        $insert_query = "INSERT INTO user_order(order_number,store_serial,destination,USER_user_serial, destination_lat,destination_long) VALUES(".$order_number.",".$store_serial.",'".$destination."',".$user_serial.",".$destination_lat.",".$destination_long.")";
        $insert_stmt = mysqli_query($connect,$insert_query);

        $query = "SELECT MAX(user_order_serial) AS u FROM Capstone.user_order";
        $stmt = mysqli_query($connect,$query);
        $row = mysqli_fetch_assoc($stmt);
        $order_serial = $row['u']+1;

        foreach($menu as $m){
            $insert_query2="INSERT INTO order_menu(user_order_serial,order_number,menu_serial) VALUES(".$order_serial.",".$order_number.",".$m.")";
            $insert_stmt2 = mysqli_query($connect,$insert_query2);
        }

        $confirm=-1;
        if($insert_stmt & $insert_stmt2)
            $confirm=1;
        else
            $confirm=0;
        $send_data=array(
            'confirm'=>$confirm
        );
        echo json_encode($send_data);

    }