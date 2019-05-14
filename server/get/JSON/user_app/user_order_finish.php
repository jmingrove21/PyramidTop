<?php
    function order_finish($json_data){
        include "../db.php";
        $order_number=$json_data['order_number'];

        $query="UPDATE store_order SET order_status=4 WHERE order_number=".$order_number;
        $stmt = mysqli_query($connect,$query);

        $confirm=0;
        if($stmt)
            $confirm=1;
        echo $confirm;
    }