<?php
require_once '../db.php';
try{
	
	//post로 받아오기
    $name=$_POST['user_name'];
	$id=$_POST['user_id'];
	$pw=$_POST['user_password'];
    $phone=$_POST['user_phone'];
    $sex=$_POST['user_sex'];

	$query = "INSERT INTO user(user_name,user_id,user_pw,user_phone,user_sex) VALUES('".$name."','".$id."','".$pw."','".$phone."','".$sex."')";
	$stmt = mysqli_query($connect,$query);
	if($stmt){
		$confirm=1;
	}else{
		$confirm=0;
	}
		
	$send_data=array(
		'confirm'=>$confirm
	);
	
	echo json_encode($send_data);
}catch(Exception $e){
	$msg = $e->getMessage();
    $line = $e->getLine();
    $code = $e->getCode();
    $error_handler = new error\ErrorHandler();
    $error_handler->logErrorReport('get/JSON/login', $code, $msg, $line, $post_dealer_serial);
    echo '{"result":"'.$code.'"}';
    exit();
}

