<?php
function modify_info($json_data){
    include "../db.php";
    $user_serial=$json_data['user_serial'];
    $user_name=$json_data['user_name'];
    $original_pw=$json_data['original_pw'];
    $change_pw=$json_data['change_pw'];
#    $user_phone=$json_data['user_phone'];
    $user_img=$json_data['user_img'];

    $check="SELECT user_pw FROM user WHERE user_serial=".$user_serial;
    $stmt=mysqli_query($connect,$check);
    $result=mysqli_fetch_assoc($stmt);

    $img=base64_decode($user_img);

#$uploaddir = '/var/www/html/get/JSON/user_image/'.$user_serial.'/';
#move_uploaded_file($_FILES['image_file']['tmp_name'], $uploaddir.$img);
#$path='http://ec2-54-180-102-7.ap-northeast-2.compute.amazonaws.com/get/JSON/user_image/'.$user_serial.'/'.$img;


    $confirm=-1;
    if($result['user_pw']===$original_pw){
        $query="UPDATE user SET user_name='".$user_name."',user_pw='".$change_pw."', user_phone='".$user_phone."' WHERE user_serial=".$user_serial;
        $stmt=mysqli_query($connect,$query);
        $confirm=1;
    }else{
        $confirm=0;
    }


            $send_data=array(
                'confirm'=>$confirm
            );
            echo json_encode($send_data);

}