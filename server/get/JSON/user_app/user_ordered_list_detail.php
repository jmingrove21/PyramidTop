<?php
    function ordered_list_detail($json_data){
        include "../db.php";
         $store_serial=$json_data['store_serial'];
         $order_number=$json_data['order_number'];

       $query="
            SELECT tb1.*,m.menu_name, menu_price,menu_count, USER_user_serial,user_id, user_phone, destination, if(make_order_time IS NULL, '',make_order_time) AS make_order_time
              FROM
              (
 				 SELECT s.order_number,s.order_create_date
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
              ORDER BY make_order_time ASC
             ";

         $stmt = mysqli_query($connect,$query);
        $user_serial=0;
        $total=[];
        $menu=[];

        while ($row = mysqli_fetch_assoc($stmt)) {
            if($user_serial===0||$user_serial==$row['USER_user_serial']){
                $user_serial=$row['USER_user_serial'];
                $user_id=$row['user_id'];
            }else{
                $data['user_menu']=$menu;
                array_push($total,$data);
                $user_serial=$row['USER_user_serial'];
                $user_id=$row['user_id'];
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
                    'user_menu'=>$menu,
                    'make_order_time'=>$row['make_order_time']
            );
        }
        $data['user_menu']=$menu;
        array_push($total,$data);
 $query="
         SELECT store_serial,store_phone, store_address_jibun, store_building_name, start_time, end_time, store_restday, store_notice, store_phone, minimum_order_price
         FROM Capstone.store
         WHERE store_serial=(SELECT store_serial FROM store_order WHERE order_number=".$order_number.")";
         $stmt = mysqli_query($connect,$query);
         while($row2=mysqli_fetch_assoc($stmt)){
            $store=array(
                'store_serial'=>$row2['store_serial'],
                'store_building_name'=>$row2['store_building_name'],
                'store_phone'=>$row2['store_phone'],
                'start_time'=>$row2['start_time'],
                'end_time'=>$row2['end_time'],
                'store_address'=>$row2['store_address_jibun'],
                'minimum_order_price'=>$row2['minimum_order_price'],
                'store_restday'=>$row2['store_restday'],
                'store_notice'=>$row2['store_notice']
            );
         }



        $send_data=array(
            'user_info'=>$total,
            'store_info'=>$store
        );

  echo json_encode($send_data,JSON_UNESCAPED_UNICODE);
    }