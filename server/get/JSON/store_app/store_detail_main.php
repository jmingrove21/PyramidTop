<?php
    function store_detail_main($json_data){
         include '../db.php';
         $store_serial=$json_data['store_serial'];
         $order_number=$json_data['order_number'];

       $query="
               SELECT m.menu_name, tb1.order_number,USER_user_serial,user_name
              FROM
              (
 				 SELECT s.order_number
 				 FROM Capstone.store_order AS s
 				 WHERE s.store_serial=".$store_serial." AND order_number=".$order_number."
              ) tb1
              INNER JOIN Capstone.order_menu AS o
              INNER JOIN Capstone.menu_info AS m
              INNER JOIN Capstone.user_order AS u
              INNER JOIN Capstone.user AS user
              ON tb1.order_number=o.order_number
              AND o.menu_serial=m.menu_serial
              AND u.user_order_serial=o.user_order_serial
              AND u.USER_user_serial=user.user_serial
              WHERE tb1.order_number=o.order_number
              ORDER BY USER_user_serial DESC
             ";
         $stmt = mysqli_query($connect,$query);

        $user_serial=0;
        $total=[];
        $menu=[];

        while ($row = mysqli_fetch_assoc($stmt)) {
            if($user_serial===0||$user_serial==$row['USER_user_serial']){
                $user_serial=$row['USER_user_serial'];
                $user_name=$row['user_name'];

            }else{
                $data['user_menu']=$menu;
                array_push($total,$data);
                $user_serial=$row['USER_user_serial'];
                $user_name=$row['user_name'];
                $menu=[];
            }

            array_push($menu,$row['menu_name']);

            $data=array(
                    'user_serial'=>$row['USER_user_serial'],
                    'user_name'=>$row['user_name'],
                    'user_menu'=>$menu
            );
        }
        $data['user_menu']=$menu;
        array_push($total,$data);

  echo json_encode($total,JSON_UNESCAPED_UNICODE);

    }