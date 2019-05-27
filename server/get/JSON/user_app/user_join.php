<?php
function user_join($json_data){
    include '../db.php';

	
	//post로 받아오기
    $name=$json_data['user_name'];
	$id=$json_data['user_id'];
	$pw=$json_data['user_password'];
    $phone=$json_data['user_phone'];

	$query = "INSERT INTO user(user_name,user_id,user_pw,user_phone) VALUES('".$name."','".$id."','".$pw."','".$phone."')";
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

}

