<?php
    function ordered_list_detail($json_data){
        include "../db.php";
         $store_serial=$json_data['store_serial'];
         $order_number=$json_data['order_number'];
         $user_serial=$json_data['user_serial'];

         $query="SELECT menu_name,menu_price,menu_count, make_order_time,arrival_time,user_pay_status,user_pay_price
              FROM
              (
 				 SELECT s.order_number
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
              WHERE tb1.order_number=".$order_number." AND u.USER_user_serial=".$user_serial."
              ORDER BY USER_user_serial DESC";
        $stmt = mysqli_query($connect,$query);
        $menu_info=[];
        while($row=mysqli_fetch_assoc($stmt)){
            $menu=array(
                'menu_name'=>$row['menu_name'],
                'menu_price'=>$row['menu_price'],
                'menu_count'=>$row['menu_count']
            );
            array_push($menu_info,$menu);
            $user_info=array(
                'make_order_time'=>$row['make_order_time'],
                'arrival_time'=>$row['arrival_time'],
                'user_menu'=>$menu_info,
                'pay_status'=>$row['user_pay_status'],
                'pay_price'=>$row['user_pay_price']
            );
        }
        $query="
            SELECT tb1.*,menu_price,menu_count, USER_user_serial,user_id,make_order_time,user_pay_price,user_pay_status
              FROM
              (
 				 SELECT s.order_number
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
              WHERE tb1.order_number=".$order_number." AND u.USER_user_serial!=".$user_serial."
              ORDER BY USER_user_serial DESC
             ";
         $stmt = mysqli_query($connect,$query);
        $user=0;
        $total=[];
        $total_price=0;

        while ($row = mysqli_fetch_assoc($stmt)) {
            if($user===0||$user==$row['USER_user_serial']){
                $user=$row['USER_user_serial'];
                $user_id=$row['user_id'];
            }else{
                array_push($total,$data);
                $user_=$row['USER_user_serial'];
                $user_id=$row['user_id'];
                $total_price=0;
            }
            $total_price+=$row['menu_price']*$row['menu_count'];

            $data=array(
                    'user_id'=>$row['user_id'],
                    'make_order_time'=>$row['make_order_time'],
                    'total_price'=>$total_price,
                    'pay_price'=>$row['user_pay_price'],
                    'pay_status'=>$row['user_pay_status']
            );
        }
        array_push($total,$data);
        $total_price=0;

        $query="
         SELECT store_serial,store_phone, store_address_jibun, store_building_name, start_time, end_time, store_restday, store_notice, store_phone, minimum_order_price, delivery_cost
         FROM Capstone.store
         WHERE store_serial=".$store_serial;
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
                'store_notice'=>$row2['store_notice'],
                'delivery_cost'=>$row2['delivery_cost']
            );
         }

        $send_data=array(
            'user_info'=>$user_info,
            'another_info'=>$total,
            'store_info'=>$store
        );

  echo json_encode($send_data,JSON_UNESCAPED_UNICODE);
    }