<?php
    function user_search_store($json_data){
        include "../db.php";
        $search=$json_data['search'];
        $user_lat=$json_data['user_lat'];
        $user_long=$json_data['user_long'];

         $query="
         SELECT store_serial,store_name, store_branch_name,store_phone, store_address_jibun, store_building_name, start_time, end_time, store_restday, store_notice, store_profile_img, store_phone, store_main_type_name, store_sub_type_name,minimum_order_price,delivery_cost,( 6371 * acos( cos( radians(".$user_lat.") ) * cos( radians( store_latitude) )
                                                                                                                                                                                                                                                                                   * cos( radians( store_longitude ) - radians(".$user_long.") )
                                                                                                                                                                                                                                                                                   + sin( radians(".$user_lat.") ) * sin( radians( store_latitude ) ) ) ) AS distance
         FROM Capstone.store
         WHERE store_name LIKE '%".$search."%'
         ORDER BY distance ASC
         ";

         $stmt = mysqli_query($connect,$query);
         $total_store=[];
         while($row2=mysqli_fetch_assoc($stmt)){
            $store=array(
                'store_serial'=>$row2['store_serial'],
                'store_name'=>$row2['store_name'],
                'store_branch_name'=>$row2['store_branch_name'],
                'store_phone'=>$row2['store_phone'],
                'store_address'=>$row2['store_address_jibun'],
                'distance'=>$row2['distance'],
                'store_building_name'=>$row2['store_building_name'],
                'start_time'=>$row2['start_time'],
                'end_time'=>$row2['end_time'],
                'store_restday'=>$row2['store_restday'],
                'store_notice'=>$row2['store_notice'],
                'store_profile_img'=>$row2['store_profile_img'],
                'store_phone'=>$row2['store_phone'],
                'store_main_type_name'=>$row2['store_main_type_name'],
                'store_sub_type_name'=>$row2['store_sub_type_name'],
                'minimum_order_price'=>$row2['minimum_order_price'],
                'delivery_cost'=>$row2['delivery_cost']
            );
            $query2="
            SELECT menu_type_code, menu_type_name, menu_code, menu_name
            FROM Capstone.menu
            WHERE store_serial=".$row2['store_serial']."
            ORDER BY menu_code
            ";
            $stmt2=mysqli_query($connect,$query2);

            $menu_type='';
            $total_menu=[];
            $menu=[];
            while($row=mysqli_fetch_assoc($stmt2)){

            if($menu_type==''||$menu_type==$row['menu_type_code']){
                $menu_type=$row['menu_type_code'];
                $menu_type_name=$row['menu_type_name'];
            }else{
                array_push($total_menu,$data);
                $menu_type=$row['menu_type_code'];
                $menu_type_name=$row['menu_type_name'];
                $menu=[];
            }

            $one_menu=array(
                'menu_code'=>$row['menu_code'],
                'menu_name'=>$row['menu_name']
            );
            array_push($menu,$one_menu);
            $data=array(
                'menu_type_code'=>$menu_type,
                'menu_type_name'=>$menu_type_name,
                'menu description'=>$menu
            );

          }
            array_push($total_menu,$data);
            $store['menu']=$total_menu;
            array_push($total_store,$store);
         }

         echo json_encode($total_store,JSON_UNESCAPED_UNICODE);
}

