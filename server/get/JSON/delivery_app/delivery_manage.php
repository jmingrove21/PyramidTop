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
    case 'approve':
        include 'delivery_approve.php';
        delivery_approve($json_data);
        break;
    case 'request':
        include 'delivery_request.php';
        delivery_request($json_data);
        break;
    case 'departure':
        include 'delivery_departure.php';
        delivery_departure($json_data);
        break;
    case 'complete':
        include 'delivery_complete.php';
        delivery_complete($json_data);
        break;
    case 'status':
        include 'delivery_status.php';
        delivery_status($json_data);
        break;
    case 'route':
        include 'delivery_route.php';
        delivery_route($json_data);
        break;
}
?>