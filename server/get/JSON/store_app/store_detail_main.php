<?php
    function store_detail_main($json_data){
         include '../db.php';
         $store_serial=$json_data['store_serial'];
         $order_number=$json_data['order_number'];

       $query="
            SELECT tb1.*,m.menu_name, menu_price,menu_count, USER_user_serial,user_id, user_phone, destination
              FROM
              (
 				 SELECT s.order_number,s.order_receipt_date
 				 FROM Capstone.store_order AS s
 				 WHERE s.store_serial=".$store_serial." AND s.order_number=".$order_number."
              ) tb1
              INNER JOIN Capstone.order_menu AS o
              INNER JOIN Capstone.menu_info AS m
              INNER JOIN Capstone.user_order AS u
              INNER JOIN Capstone.user AS user
              ON tb1.order_number=o.order_number
              AND o.menu_code=m.menu_code
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
                $user_id=$row['user_id'];
                $user_phone=$row['user_phone'];
            }else{
                $data['user_menu']=$menu;
                array_push($total,$data);
                $user_serial=$row['USER_user_serial'];
                $user_id=$row['user_id'];
                $user_phone=$row['user_phone'];
                $menu=[];
            }
             $menu_info=array(
                'menu_name'=>$row['menu_name'],
                'menu_price'=>$row['menu_price'],
                'menu_count'=>$row['menu_count']
             );
             array_push($menu,$menu_info);

            $data=array(
                    'user_serial'=>$row['USER_user_serial'],
                    'user_id'=>$row['user_id'],
                    'user_phone'=>$row['user_phone'],
                    'destination'=>$row['destination'],
                    'user_menu'=>$menu,
                    'order_receipt_date'=>$row['order_receipt_date']
            );
        }
        $data['user_menu']=$menu;
        array_push($total,$data);

  echo json_encode($total,JSON_UNESCAPED_UNICODE);

    }