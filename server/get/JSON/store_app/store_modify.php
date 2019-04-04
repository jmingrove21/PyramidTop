<?php
        include '../db.php';
        header('Access-Control-Allow-Origin: *');
        header('Access-Control-Allow-Methods: GET,POST,PUT');
        header('Access-Control-Allow-Headers: X-Requested-With,Content-Type');
        header('Content-Type: text/html; charset=utf-8');
        $uploaddir = '/var/www/html/image/';

        $store_serial=$_POST['store_serial'];
        $store_name=$_POST['store_name'];
        $storemaster_name=$_POST['storemaster_name'];
        $store_address=$_POST['store_address'];
        $start_time=$_POST['start_time'];
        $end_time=$_POST['end_time'];
        $store_restday=$_POST['store_restday'];
        $store_notice=$_POST['store_notice'];
        $store_phone=$_POST['store_phone'];
        $store_profile_img=$_FILES["image_file"];
        $upload=$_FILES['image_file']['name'];

        $a=move_uploaded_file($_FILES['image_file']['tmp_name'], $uploaddir.$upload);

        $path='http://ec2-54-180-102-7.ap-northeast-2.compute.amazonaws.com/image/'.$upload;

        $query = "UPDATE store SET store_name='".$store_name."',store_address='".$store_address."', start_time='".$start_time."', end_time='".$end_time."', store_restday='".$store_restday."', store_notice='".$store_notice."',  store_profile_img='".$path."' , store_phone='".$store_phone."' WHERE store_serial='".$store_serial."' LIMIT 1";
        $stmt = mysqli_query($connect,$query);

        $query2="UPDATE store_master SET  store_master_name='".$storemaster_name."'  WHERE store_serial='".$store_serial."' LIMIT 1";
        $stmt2 = mysqli_query($connect,$query2);
        $confirm=-1;
        if($stmt && $stmt2)
            $confirm=1;
        else
            $confirm=0;

        $send_data=array(
            'confirm' =>$confirm,
            'path'=>$path
        );
        echo json_encode($send_data);
