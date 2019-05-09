<?php
    function delivery_request($json_data){
        include '../db.php';
        $delivery_lat=$json_data['delivery_lat'];
        $delivery_long=$json_data['delivery_long'];

        $query="
                    SELECT so.order_number, so.store_serial, delivery_request_time, s.store_name, s.store_branch_name,( 6371 * acos( cos( radians(".$delivery_lat.") ) * cos( radians( store_latitude) )
                                                                                                                                                              * cos( radians( store_longitude ) - radians(".$delivery_long.") )
                                                                                                                                                              + sin( radians(".$delivery_lat.") ) * sin( radians( store_latitude ) ) ) ) AS distance
        			FROM Capstone.store_order AS so
        			INNER JOIN Capstone.store AS s
        			ON so.store_serial=s.store_serial
                    WHERE so.order_status=4
                    ORDER BY delivery_request_time ASC
        ";
        $stmt = mysqli_query($connect,$query);

        $total=[];

         while ($row = mysqli_fetch_assoc($stmt)) {
            $data=array(
                'order_number'=>$row['order_number'],
                'store_serial'=>$row['store_serial'],
                'delivery_request_time'=>$row['delivery_request_time'],
                'store_name'=>$row['store_name'],
                'store_branch_name'=>$row['store_branch_name'],
                'distance'=>$row['distance']
            );
            array_push($total,$data);

         }


        echo json_encode($total,JSON_UNESCAPED_UNICODE);


    }