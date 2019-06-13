<?php
 function ordered_list($json_data){
        include "../db.php";
        $user_serial=$json_data['user_serial'];
        $order_info=$json_data['order_info'];
        if($order_info==1)
            $sub_query=" NOT BETWEEN 7 AND 8";
        else if($order_info==0)
            $sub_query="=7";

         $query="
         SELECT so.store_serial,order_status,store_name, store_phone,store_branch_name,store_address_jibun,store_profile_img,minimum_order_price, if(order_create_date IS NULL, '',order_create_date) AS order_create_date, if(order_receipt_date IS NULL, '',order_receipt_date) AS order_receipt_date, if(delivery_request_time IS NULL, '',delivery_request_time) AS delivery_request_time, if(delivery_approve_time IS NULL, '',delivery_approve_time) AS delivery_approve_time, if(delivery_departure_time IS NULL, '' ,delivery_departure_time) AS delivery_departure_time, so.order_number
         FROM Capstone.store AS s
         INNER JOIN Capstone.store_order AS so
         INNER JOIN Capstone.user_order AS uo
         ON s.store_serial=so.store_serial AND so.order_number=uo.order_number
         WHERE order_status".$sub_query." AND uo.USER_user_serial=".$user_serial."
         ORDER BY make_order_time DESC
         ";
         $stmt = mysqli_query($connect,$query);
         $total_store=[];

         while($row2=mysqli_fetch_assoc($stmt)){
            $count_query="
                    SELECT count(*) AS c
                    FROM Capstone.user_order
                    WHERE order_number=".$row2['order_number']." AND user_status!=8";
            $count_stmt = mysqli_query($connect,$count_query);
            $result=mysqli_fetch_assoc($count_stmt);
            $persons=$result['c'];

            $confirm_query="
            SELECT tb.*,menu_price
            FROM Capstone.menu AS m
            INNER JOIN
            (
                SELECT menu_code,menu_count
                FROM Capstone.order_menu
                WHERE order_number=".$row2['order_number']."
            ) tb
            ON m.menu_code=tb.menu_code
            WHERE store_serial=".$row2['store_serial'];

            $confirm_stmt = mysqli_query($connect,$confirm_query);
            $total_price=0;
            while($row=mysqli_fetch_assoc($confirm_stmt)){
                $total_price+=$row['menu_price']*$row['menu_count'];
            }

            $store=array(
                'order_number'=>$row2['order_number'],
                'order_status'=>$row2['order_status'],
                'store_serial'=>$row2['store_serial'],
                'store_name'=>$row2['store_name'],
                'store_branch_name'=>$row2['store_branch_name'],
                'minimum_order_price'=>$row2['minimum_order_price'],
                'store_profile_img'=>$row2['store_profile_img'],
                'order_create_date'=>$row2['order_create_date'],
                'order_receipt_date'=>$row2['order_receipt_date'],
                'delivery_request_time'=>$row2['delivery_request_time'],
                'delivery_approve_time'=>$row2['delivery_approve_time'],
                'delivery_departure_time'=>$row2['delivery_departure_time'],
                'participate_persons'=>$persons,
                'total_order_price'=>$total_price,
                'make_order_time'=>$row2['make_order_time']
            );

            array_push($total_store,$store);
         }
         echo json_encode($total_store,JSON_UNESCAPED_UNICODE);

    }