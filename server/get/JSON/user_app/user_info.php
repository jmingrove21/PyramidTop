<?php
function info($json_data){
    include "../db.php";
    $user_serial=$json_data['user_serial'];
    $query="SELECT * FROM user WHERE user_serial=".$user_serial;
    $stmt = mysqli_query($connect,$query);
    $row=mysqli_fetch_assoc($stmt);
    $data=array(
        'user_name'=>$row['user_name'],
        'user_id'->$row['user_id'],
        'user_pw'=>$row['user_pw'],
        'user_phone'=>$row['user_phone'],
        'user_mileage'=>$row['user_mileage'],
        'user_img'=>$row['user_img']
    );
    echo json_encode($data,JSON_UNESCAPED_UNICODE);
}