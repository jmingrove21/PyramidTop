<?php
    include "../db.php";
    $store_serial=$_POST['store_serial'];
    $menu_name=$_POST['menu_name'];
    $menu_price=$_POST['menu_price'];
    $menu_img=$_FILES['menu_img'];
    $menu_type_code=$_POST['menu_type_code'];
    $upload=$_FILES['menu_img']['name'];

        $store_query="SELECT store_id FROM store WHERE store_serial=".$store_serial;
        $stmt=mysqli_query($connect,$store_query);
        $row=mysqli_fetch_assoc($stmt);

        $uploaddir = '/var/www/html/get/JSON/image/'.$row['store_id'].'/';
        move_uploaded_file($_FILES['image_file']['tmp_name'], $uploaddir.$upload);

        $path='http://ec2-54-180-102-7.ap-northeast-2.compute.amazonaws.com/get/JSON/image/'.$row['store_id'].'/'.$upload;

        $query="SELECT store_main_type_code,store_main_type_name,store_sub_type_code,store_sub_type_name,menu_type_name,RIGHT(menu_code,1) AS code FROM Capstone.menu WHERE menu_type_code='".$menu_type_code."' AND store_serial=".$store_serial." ORDER BY menu_code DESC LIMIT 1";

        $stmt=mysqli_query($connect,$query);
        $row=mysqli_fetch_assoc($stmt);
        $code=$row['code']+1;
        $menu_code=$menu_type_code.'C0'.$code;
    

        $insert_query="
        INSERT INTO
        menu(store_serial,store_main_type_code,store_main_type_name,store_sub_type_code,store_sub_type_name,menu_type_code,menu_type_name,menu_code,menu_name,menu_price,menu_img)
        VALUES(".$store_serial.",'".$row['store_main_type_code']."','".$row['store_main_type_name']."','".$row['store_sub_type_code']."','".$row['store_sub_type_name']."','".$menu_type_code."','".$row['menu_type_name']."','".$menu_code."','".$menu_name."',".$menu_price.",'".$path."')
        ";
        $stmt=mysqli_query($connect,$insert_query);

        $confirm=-1;
        if($stmt)
            $confirm=1;
        else
            $confirm=0;

        $send_data=array(
            'confirm'=>$confirm
        );
        echo json_encode($send_data);
