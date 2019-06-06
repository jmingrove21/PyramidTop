<?php
function delivery_status($json_data){
    include "../db.php";
    $delivery_id=$json_data['delivery_id'];
    $query="SELECT delivery_status,delivery_order_number FROM delivery WHERE delivery_id='".$delivery_id."'";
    $stmt = mysqli_query($connect,$query);
    $row=mysqli_fetch_assoc($stmt);

    $query1="SELECT store_name,store_branch_name FROM store WHERE store_serial=(SELECT store_serial FROM store_order WHERE order_number=".$row['delivery_order_number'].")";
    $stmt1 = mysqli_query($connect,$query1);
    $row1=mysqli_fetch_assoc($stmt1);

    $send_data=array(
        'status'=>$row['delivery_status'],
        'order_number'=>$row['delivery_order_number'],
        'store_name'=>$row1['store_name'],
        'store_branch_name'=>$row1['store_branch_name']
    );
    echo json_encode($send_data,JSON_UNESCAPED_UNICODE);
}