<?php
    include "../db.php";
    $user_serial=$_POST['user_serial'];
    $user_name=$_POST['user_name'];
    $original_pw=$_POST['original_pw'];
    $change_pw=$_POST['change_pw'];
    $user_img=$_FILES['user_img'];
    $upload=$_FILES['user_img']['name'];
    $img_check=$_POST['img_check'];

    $check="SELECT user_pw FROM user WHERE user_serial=".$user_serial;
    $stmt=mysqli_query($connect,$check);
    $result=mysqli_fetch_assoc($stmt);

    if($img_check==1){
        $uploaddir = '/var/www/html/get/JSON/user_image/'.$user_serial.'/';
        move_uploaded_file($_FILES['image_file']['tmp_name'], $uploaddir.$img);
        $path='http://ec2-54-180-102-7.ap-northeast-2.compute.amazonaws.com/get/JSON/user_image/'.$user_serial.'/'.$upload;
        $sub_query="user_img='".$path."'";
    }else if($img_check==0){
        $sub_query="";
    }
    $confirm=-1;
    if($result['user_pw']===$original_pw){
        $query="UPDATE user SET ".$sub_query." user_name='".$user_name."',user_pw='".$change_pw."', user_phone='".$user_phone."' WHERE user_serial=".$user_serial;
        $stmt=mysqli_query($connect,$query);
        $confirm=1;
    }else{
        $confirm=0;
    }


            $send_data=array(
                'confirm'=>$confirm
            );
     echo json_encode($send_data);

