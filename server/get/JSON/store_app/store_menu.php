<?php
    function store_menu($json_data){
         include '../db.php';
         $store_serial=$json_data['store_serial'];

         $query="
         SELECT menu_type_code, menu_type_name, menu_code, menu_name
         FROM Capstone.menu AS m
         INNER JOIN Capstone.store AS s
         ON m.store_serial=s.store_serial
         WHERE m.store_serial=".$store_serial." ORDER BY menu_code;
         ";
         $stmt = mysqli_query($connect,$query);

         $menu_type='';
         $total=[];
         $menu=[];
         while($row=mysqli_fetch_assoc($stmt)){
            if($menu_type==''||$menu_type==$row['menu_type_code']){
                $menu_type=$row['menu_type_code'];
                $menu_type_name=$row['menu_type_name'];
            }else{
                array_push($total,$data);
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
         }
         array_push($total,$data);

         echo json_encode($total,JSON_UNESCAPED_UNICODE);


}