<?php
require_once '../db.php';
try{
	
	//post로 받아오기

	$store_name=$_POST['store_name'];
	$store_id=$_POST['store_id'];
	$store_pw=$_POST['store_pw'];
    $store_phone=$_POST['store_phone'];
    $store_address=$_POST['store_address'];
    $store_point_x=$_POST['store_point_x'];
    $store_point_y=$_POST['store_point_y'];
    $start_time=$_POST['start_time'];
    $end_time=$_POST['end_time'];
    $oper_time=$_POST['oper_time'];
    $storemaster_name=$_POST['storemaster_name'];
    $storemaster_phone=$_POST['storemaster_phone'];
    $storemaster_rrn=$_POST['storemaster_rrn'];

	$query = "INSERT INTO STORE(store_name,store_id,store_pw,store_phone,store_address,store_point_x,store_point_y,start_time,end_time,oper_time,storemaster_name,storemaster_phone,storemaster_rrn) VALUES('".$store_name."','".$store_id."','".$store_pw."','".$store_phone."','".$store_address."','".$store_point_x."','".$store_point_y."','".$start_time."','".$end_time."','".$oper_time."','".$storemaster_name."','".$storemaster_phone."','".$storemaster_rrn."')";
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

