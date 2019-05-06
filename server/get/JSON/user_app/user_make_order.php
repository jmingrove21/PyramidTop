<?php
    function user_make_order($json_data){
        include "../db.php";
        $user_serial=$json_data['user_serial'];
        $store_serial=$json_data['store_serial'];
        $destination=$json_data['destination'];
        $destination_lat=$json_data['destination_lat'];
        $destination_long=$json_data['destination_long'];
        $menu=$json_data['menu'];

        $query="
        SELECT MAX(order_number) AS m FROM user_order
        ";
        $stmt = mysqli_query($connect,$query);
        $row=mysqli_fetch_assoc($stmt);
        $order_num=$row['m']+1;


        $insert_query="INSERT INTO Capstone.user_order (order_number,store_serial,destination,USER_user_serial, destination_lat,destination_long) VALUES(".$order_num.",".$store_serial.",'".$destination."',".$user_serial.",".$destination_lat.",".$destination_long.")";
        $insert_stmt = mysqli_query($connect,$insert_query);

        $query="
        SELECT MAX(user_order_serial) AS u FROM user_order
        ";
        $stmt = mysqli_query($connect,$query);
        $row=mysqli_fetch_assoc($stmt);
        $order_serial=$row['u']+1;

        foreach($menu as $m){
            $insert_query2="INSERT INTO Capstone.order_menu (user_order_serial, order_number, menu_serial) VALUES(".$order_serial.",".$order_num.",'".$m."')";
            $insert_stmt2=mysqli_query($connect, $insert_query2);
        }

        $confirm=-1;
        if($insert_stmt&$insert_stmt2)
            $confirm=1;
        else
            $confirm=0;
        $send_data=array(
            'confirm'=>$confirm,
            'order_number'=>$order_num
        );
        echo json_encode($send_data);
    }