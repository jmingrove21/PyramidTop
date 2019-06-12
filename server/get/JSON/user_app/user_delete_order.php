<?php
    function delete_order($json_data){
        include "../db.php";
        $order_number=$json_data['order_number'];
        $user_serial=$json_data['user_serial'];

        $query="UPDATE user_order SET user_status=8 WHERE order_number=".$order_number." AND USER_user_serial=".$user_serial;

        print_r($query);
        $stmt = mysqli_query($connect,$query);
        $confirm=-1;
        if($stmt)
            $confirm=1;
        else
            $confirm=0;

        $send_data=array(
            'confirm'=>$confirm
        );
        echo json_encode($send_data);


    }