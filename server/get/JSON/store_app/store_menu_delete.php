<?php
function menu_delete($json_data){
    include "../db.php";
    $store_serial=$json_data['store_serial'];
    $menu_code=$json_data['menu_code'];
    $query="DELETE FROM menu WHERE store_serial=".$store_serial." AND menu_code='".$menu_code."'";
    $stmt=mysqli_query($connect,$query);

       $confirm=-1;
        if($stmt)
            $confirm=1;
        else
            $confirm=0;

        $send_data=array(
            'confirm'=>$confirm
        );
        echo json_encode($send_data);
}