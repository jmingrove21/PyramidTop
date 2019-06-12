<?php
    include '../db.php';
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET,POST,PUT');
    header('Access-Control-Allow-Headers: X-Requested-With,Content-Type');
    header('Content-Type: text/html; charset=utf-8');

    $name=$_POST['user_name'];
	$id=$_POST['user_id'];
	$pw=$_POST['user_password'];
    $phone=$_POST['user_phone'];
    $user_img=$_FILES['user_img'];
    $upload=$_FILES['user_img']['name'];

	$query = "INSERT INTO user(user_name,user_id,user_pw,user_phone) VALUES('".$name."','".$id."','".$pw."','".$phone."')";
	$stmt = mysqli_query($connect,$query);
	if($stmt){
		$confirm=1;
	}else{
		$confirm=0;
	}

    $query="SELECT user_serial FROM user WHERE user_id='".$id."'";
    $stmt = mysqli_query($connect,$query);
    $row=mysqli_fetch_assoc($stmt);
    chdir("/var/www/html/get/JSON/user_image");
    mkdir("{$row['user_serial']}");

    $uploaddir = '/var/www/html/get/JSON/user_image/'.$row['user_serial'].'/';
    move_uploaded_file($_FILES['image_file']['tmp_name'], $uploaddir.$upload);
    $path='http://ec2-54-180-102-7.ap-northeast-2.compute.amazonaws.com/get/JSON/user_image/'.$row['user_serial'].'/'.$upload;

    $update="UPDATE user SET user_img='".$path."' WHERE user_serial=".$row['user_serial'];
    $stmt = mysqli_query($connect,$update);

	$send_data=array(
		'confirm'=>$confirm
	);

	echo json_encode($send_data);



