<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET,POST,PUT');
header('Access-Control-Allow-Headers: X-Requested-With,Content-Type');
require_once 'db.php';
try{
	
	//post로 받아오기
	//$id = preg_replace('/^[a-z0-9_]{4,20}$/','',$_POST['store_id']);
	//$pw = preg_replace('/^[a-z0-9_]{4,20}$/','',$_POST['store_password']);

	$id=$_POST['store_id'];
	$pw=$_POST['store_password'];	
	$query = "SELECT COUNT(*) AS num FROM test AS t WHERE t.id='".$id."' AND t.password='".$pw."'";
	$stmt = mysqli_query($connect,$query);
	$result = mysqli_fetch_assoc($stmt);	
		
	if($result['num']==='0'){
		$confirm=0; //login fail
	}else{
		$confirm=1; //login success
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

