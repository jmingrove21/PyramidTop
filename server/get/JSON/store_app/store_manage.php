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
    case 'main':
        include 'store_main.php';
        store_main($json_data);
        break;
    case 'detail_main':
        include 'store_detail_main.php';
        store_detail_main($json_data);
        break;
    case 'complete_order':
        include 'store_complete_order.php';
        store_complete_order($json_data);
        break;
    case 'history':
        include 'store_order_history.php';
        store_order_history($json_data);
        break;
    case 'menu':
        include 'store_menu.php';
        store_menu($json_data);
        break;
    case 'one_menu_info':
        include 'store_one_menu_info.php';
        one_menu_info($json_data);
        break;
    case 'menu_delete':
        include 'store_menu_delete.php';
        menu_delete($json_data);
        break;

}
?>