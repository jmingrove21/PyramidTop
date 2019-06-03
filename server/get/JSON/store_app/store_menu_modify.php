<?php
 include '../db.php';
        header('Access-Control-Allow-Origin: *');
        header('Access-Control-Allow-Methods: GET,POST,PUT');
        header('Access-Control-Allow-Headers: X-Requested-With,Content-Type');
        header('Content-Type: text/html; charset=utf-8');

        $store_serial=$_POST['store_serial'];
        $menu_code=$_POST['menu_code'];
        $menu_img=$_FILES['menu_img'];
        $menu_name=$_POST['menu_name'];
        $menu_price=$_POST['menu_price'];
        $upload=$_FILES['menu_img']['name'];

        $store_query="SELECT store_id FROM store WHERE store_serial=".$store_serial;
        $stmt=mysqli_query($connect,$store_query);
        $row=mysqli_fetch_assoc($stmt);

        $uploaddir = '/var/www/html/get/JSON/image/'.$row['store_id'].'/';
        move_uploaded_file($_FILES['image_file']['tmp_name'], $uploaddir.$upload);

        $path='http://ec2-54-180-102-7.ap-northeast-2.compute.amazonaws.com/get/JSON/image/'.$row['store_id'].'/'.$upload;

        $query="UPDATE menu SET menu_img='".$path."', menu_name='".$menu_name."', menu_price=".$menu_price." WHERE store_serial=".$store_serial." AND menu_code='".$menu_code."'";

        $stmt=mysqli_query($connect,$query);

        $confirm=-1;
        if($stmt)
            $confirm=1;
        else
            $confirm=0;

        $send_data=array(
            'confirm'=>$confirm,
            'path'=>$path
        );
        echo json_encode($send_data);
