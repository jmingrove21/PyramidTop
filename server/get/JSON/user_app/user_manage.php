<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET,POST,PUT');
header('Access-Control-Allow-Headers: X-Requested-With,Content-Type');
header('Content-Type: text/html; charset=utf-8');
$json_data = json_decode(file_get_contents('php://input'), TRUE);
$user_info=$json_data['user_info'];

switch($user_info){
    case 'login':
        include 'user_login.php';
        user_login($json_data);
        break;
    case 'join':
        include 'user_join.php';
        user_join($json_data);
        break;
    case 'main':
        include 'user_main.php';
        user_main($json_data);
        break;
    case 'store_info':
        include 'user_store_info.php';
        store_info($json_data);
        break;
    case 'make_order':
        include 'user_make_order.php';
        user_make_order($json_data);
        break;
    case 'participate_order':
        include 'user_participate_order.php';
        participate_order($json_data);
        break;
    case 'search_store':
        include 'user_search_store.php';
        user_search_store($json_data);
        break;
    case 'lookup_participate':
        include 'user_lookup_participate.php';
        lookup_participate($json_data);
        break;
    case 'ordered_list':
        include 'user_ordered_list.php';
        ordered_list($json_data);
        break;
    case 'ordered_list_detail':
        include 'user_ordered_list_detail.php';
        ordered_list_detail($json_data);
        break;
    case 'store_detail':
        include 'user_store_detail.php';
        store_detail($json_data);
        break;
}
?>