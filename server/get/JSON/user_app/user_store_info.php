<?php
    function store_info($json_data){
        include '../db.php';
        $store_serial=$json_data['store_serial'];
         $query="
         SELECT store_name, store_branch_name, store_address_jibun, store_building_name, start_time, end_time, store_restday, store_notice, store_profile_img, store_phone, s.store_main_type_name, s.store_sub_type_name, menu_type_code, menu_type_name, menu_code, menu_name
         FROM Capstone.menu AS m
         INNER JOIN Capstone.store AS s
         ON m.store_serial=s.store_serial
         WHERE m.store_serial=".$store_serial." ORDER BY menu_code;
         ";
         $stmt = mysqli_query($connect,$query);

         $menu_type='';
         $total_menu=[];
         $menu=[];
         while($row=mysqli_fetch_assoc($stmt)){
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
                'menu'=>$menu
            );
            $total=array(
                'store_name'=>$row['store_name'],
                'store_branch_name'=>$row['store_branch_name'],
                'store_address_jibun'=>$row['store_address_jibun'],
                'store_building_name'=>$row['store_building_name'],
                'start_time'=>$row['start_time'],
                'end_time'=>$row['end_time'],
                'store_restday'=>$row['store_restday'],
                'store_notice'=>$row['store_notice'],
                'store_profile_img'=>$row['store_profile_img'],
                'store_phone'=>$row['store_phone'],
                'store_main_type_name'=>$row['store_main_type_name'],
                'store_sub_type_name'=>$row['store_sub_type_name'],
                'menu'=>$total_menu
            );
         }
         array_push($total_menu,$data);
         $total['menu']=$total_menu;

         echo json_encode($total,JSON_UNESCAPED_UNICODE);
}

