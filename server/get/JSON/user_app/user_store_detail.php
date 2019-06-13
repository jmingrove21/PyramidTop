<?php
    function store_detail($json_data){
        include "../db.php";
        $store_serial=$json_data['store_serial'];

         $query="
         SELECT store_serial,store_phone, store_address_jibun, store_building_name, start_time, end_time, store_restday, store_notice, store_phone, store_main_type_name, store_sub_type_name, minimum_order_price,delivery_cost
         FROM Capstone.store
         WHERE store_serial=".$store_serial;
         $stmt = mysqli_query($connect,$query);

         while($row2=mysqli_fetch_assoc($stmt)){
            $store=array(
                'store_serial'=>$row2['store_serial'],
                'store_building_name'=>$row2['store_building_name'],
                'start_time'=>$row2['start_time'],
                'end_time'=>$row2['end_time'],
                'store_restday'=>$row2['store_restday'],
                'store_notice'=>$row2['store_notice'],
                'store_main_type_name'=>$row2['store_main_type_name'],
                'store_sub_type_name'=>$row2['store_sub_type_name'],
                'minimum_order_price'=>$row2['minimum_order_price'],
                'delivery_cost'=>$row2['delivery_cost']
            );
            $query2="
            SELECT menu_type_code, menu_type_name, menu_code, menu_name, menu_price, menu_img
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
                'menu_name'=>$row['menu_name'],
                'menu_price'=>$row['menu_price'],
                'menu_img'=>$row['menu_img'],
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
         }

         echo json_encode($store,JSON_UNESCAPED_UNICODE);
    }