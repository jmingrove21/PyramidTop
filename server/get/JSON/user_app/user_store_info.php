<?php
    function store_info($json_data){
        include '../db.php';
        $user_lat=$json_data['user_lat'];
        $user_long=$json_data['user_long'];
        $count=$json_data['count'];
        $store_type = $json_data['store_type'];


         $query="
         SELECT store_serial,store_name,store_phone,store_branch_name,store_address_jibun,store_profile_img, minimum_order_price,( 6371 * acos( cos( radians(".$user_lat.") ) * cos( radians( store_latitude) )
                                                                                                                                                                                                                                                                                   * cos( radians( store_longitude ) - radians(".$user_long.") )
                                                                                                                                                                                                                                                                                   + sin( radians(".$user_lat.") ) * sin( radians( store_latitude ) ) ) ) AS distance
         FROM Capstone.store
         WHERE store_main_type_code='".$store_type."'
         ORDER BY distance ASC LIMIT ".$count.",5
         ";

         $stmt = mysqli_query($connect,$query);
         $total_store=[];
         while($row2=mysqli_fetch_assoc($stmt)){
            $store=array(
                'store_serial'=>$row2['store_serial'],
                'store_name'=>$row2['store_name'],
                'store_branch_name'=>$row2['store_branch_name'],
                'store_address'=>$row2['store_address_jibun'],
                'store_phone'=>$row2['store_phone'],
                'minimum_order_price'=>$row2['minimum_order_price'],
                'distance'=>$row2['distance'],
                'store_profile_img'=>$row2['store_profile_img']
            );

            array_push($total_store,$store);
         }

         echo json_encode($total_store,JSON_UNESCAPED_UNICODE);
}

