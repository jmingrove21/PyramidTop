<?php
    function store_main($json_data){
         include '../db.php';
         $store_serial=$json_data['store_serial'];

       $query="
            SELECT tb1.order_receipt_date, m.menu_name, tb1.order_number,tb1.order_status,USER_user_serial
             FROM
             (
				 SELECT s.order_number, s.order_status, s.order_receipt_date
				 FROM Capstone.store_order AS s
				 WHERE s.store_serial=".$store_serial."
             ) tb1
             INNER JOIN Capstone.order_menu AS o
             INNER JOIN Capstone.menu_info AS m
             INNER JOIN Capstone.user_order AS u
             ON tb1.order_number=o.order_number
             AND o.menu_serial=m.menu_serial
             AND u.user_order_serial=o.user_order_serial
             WHERE tb1.order_number=o.order_number AND tb1.order_status BETWEEN 3 AND 5
             ORDER BY tb1.order_receipt_date,USER_user_serial DESC
             ";
         $stmt = mysqli_query($connect,$query);

         $order_num=0;
         $user_serial=0;
         $total=[];
         $menu=[];
         $user_order=[];

        while ($row = mysqli_fetch_assoc($stmt)) {

            if($order_num===0||$order_num==$row['order_number']){
                $order_num=$row['order_number'];
            }else{
                array_push($user_order,$user_menu);
                $data=array(
                    'order_status'=>$row['order_status'],
                    'order_num'=>$order_num,
                    'date'=>$row['order_receipt_date'],
                    'user_order'=>$user_order
                );
                array_push($total,$data);
                $order_num=$row['order_number'];
                $user_order=[];
                $user_serial=$row['USER_user_serial'];
                                $menu=[];
            }
            if($user_serial===0||$user_serial==$row['USER_user_serial']){
                $user_serial=$row['USER_user_serial'];

            }else{
                array_push($user_order,$user_menu);
                $user_serial=$row['USER_user_serial'];
                $menu=[];
            }

            array_push($menu,$row['menu_name']);
            $user_menu=array(
                                'user_serial'=>$row['USER_user_serial'],
                                'menu'=>$menu
                );

            $data=array(
                    'order_status'=>$row['order_status'],
                    'order_num'=>$order_num,
                    'date'=>$row['order_receipt_date'],
                    'user_order'=>$user_order
            );
        }
        array_push($user_order,$user_menu);
        $data['user_order']=$user_order;
        array_push($total,$data);

  echo json_encode($total,JSON_UNESCAPED_UNICODE);

    }