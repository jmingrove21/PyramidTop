<?php

    function lookup_participate($json_data){
    include "../db.php";
        $user_lat=$json_data['user_lat'];
        $user_long=$json_data['user_long'];
        $count=$json_data['user_count'];

         $query="
         SELECT so.store_serial,store_name, store_phone,store_branch_name,store_address_jibun,store_profile_img,minimum_order_price,order_create_date,order_number,( 6371 * acos( cos( radians(".$user_lat.") ) * cos( radians( store_latitude) )
                                                                                                                                                                                                                                                                                   * cos( radians( store_longitude ) - radians(".$user_long.") )
                                                                                                                                                                                                                                                                                   + sin( radians(".$user_lat.") ) * sin( radians( store_latitude ) ) ) ) AS distance
         FROM Capstone.store AS s
         INNER JOIN Capstone.store_order AS so
         ON s.store_serial=so.store_serial
         WHERE order_status=1
         ORDER BY distance ASC
         ";

         $stmt = mysqli_query($connect,$query);
         $total_store=[];

         while($row2=mysqli_fetch_assoc($stmt)){
            $count_query="
                    SELECT count(*) AS c
                    FROM Capstone.user_order
                    WHERE order_number=".$row2['order_number'];
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
                'store_serial'=>$row2['store_serial'],
                'store_name'=>$row2['store_name'],
                'store_phone'=>$row2['store_phone'],
                'store_branch_name'=>$row2['store_branch_name'],
                'store_address'=>$row2['store_address_jibun'],
                'minimum_order_price'=>$row2['minimum_order_price'],
                'distance'=>$row2['distance'],
                'store_profile_img'=>$row2['store_profile_img'],
                'order_create_date'=>$row2['order_create_date'],
                'participate_persons'=>$persons,
                'total_order_price'=>$total_price
            );
            array_push($total_store,$store);
         }
         echo json_encode($total_store,JSON_UNESCAPED_UNICODE);

    }