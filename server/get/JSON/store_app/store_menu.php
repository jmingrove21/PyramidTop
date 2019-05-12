<?php
    function store_menu($json_data){
        include "../db.php";

        $store_serial=$json_data['store_serial'];

        $query="SELECT menu_type_code,menu_type_name,menu_code,menu_name,menu_price, menu_img FROM menu WHERE store_serial=".$store_serial;
        $stmt = mysqli_query($connect,$query);

        $menu=[];
        while ($row = mysqli_fetch_assoc($stmt)) {
            $data=array(
                'menu_type_code'=>$row['menu_type_code'],
                'menu_type_name'=>$row['menu_type_name'],
                'menu_code'=>$row['menu_code'],
                'menu_name'=>$row['menu_name'],
                'menu_price'=>$row['menu_price'],
                'menu_img'=>$row['menu_img']
            );
            array_push($menu,$data);
        }
        echo json_encode($menu,JSON_UNESCAPED_UNICODE);
    }