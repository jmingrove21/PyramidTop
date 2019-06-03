<?php
    function one_menu_info($json_data){
        include "../db.php";
        $store_serial=$json_data['store_serial'];
        $menu_code=$json_data['menu_code'];

        $query="SELECT menu_type_code,menu_type_name,menu_code,menu_name,menu_price, menu_img FROM menu WHERE store_serial=".$store_serial." AND menu_code='".$menu_code."'";
        $stmt = mysqli_query($connect,$query);
        $row = mysqli_fetch_assoc($stmt);
        $menu=array(
                'menu_type_code'=>$row['menu_type_code'],
                'menu_type_name'=>$row['menu_type_name'],
                'menu_code'=>$row['menu_code'],
                'menu_name'=>$row['menu_name'],
                'menu_price'=>$row['menu_price'],
                'menu_img'=>$row['menu_img']
        );
        $data=[];
        array_push($data,$menu);
        echo json_encode($data,JSON_UNESCAPED_UNICODE);
    }