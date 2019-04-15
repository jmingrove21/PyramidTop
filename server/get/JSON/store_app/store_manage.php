<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET,POST,PUT');
header('Access-Control-Allow-Headers: X-Requested-With,Content-Type');
header('Content-Type: text/html; charset=utf-8');
$json_data = json_decode(file_get_contents('php://input'), TRUE);
$store_info=$json_data['store_info'];

switch($store_info){
    case 'login':
        include 'store_login.php';
        store_login($json_data);
        break;
    case 'info':
        include 'store_info.php';
        store_info($json_data);
        break;
    case 'order':
        include 'store_order.php';
        store_order($json_data);
        break;



}
?>