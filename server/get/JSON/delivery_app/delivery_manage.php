<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET,POST,PUT');
header('Access-Control-Allow-Headers: X-Requested-With,Content-Type');
$json_data = json_decode(file_get_contents('php://input'), TRUE);
$delivery_info=$json_data['delivery_info'];

switch($delivery_info){
    case 'login':
        include 'delivery_login.php';
        delivery_login($json_data);
        break;
    case 'order':
        include 'delivery_order.php';
        delivery_order($json_data);
        break;
    case 'request':
        include 'delivery_request.php';
        delivery_request($json_data);
        break;
}
?>