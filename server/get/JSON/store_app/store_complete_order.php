<?php
    function store_complete_order($json_data){
        include '../db.php';
        $store_serial=$json_data['store_serial'];
        $order_num=$json_data['order_num'];
        $query="UPDATE store_order SET order_status=4 WHERE order_number=".$order_num." AND store_serial=".$store_serial;
        $stmt = mysqli_query($connect,$query);

        $confirm=-1;
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