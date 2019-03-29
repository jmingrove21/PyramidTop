<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET,POST,PUT');
header('Access-Control-Allow-Headers: X-Requested-With,Content-Type');
$json_data = json_decode(file_get_contents('php://input'), TRUE);
$delivery_info=$json_data['delivery_info'];

switch($delivery_info){
    case 'login':
        include 'JSON/user_login.php';
        user_login($json_data);
        break;

}
?>