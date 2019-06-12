<?php
    function delete_order($json_data){
        include "../db.php";
        $order_number=$json_data['order_number'];
        $user_serial=$json_data['user_serial'];

        $status_query="SELECT order_status FROM store_order WHERE order_number=".$order_number;
        $status_stmt=mysqli_query($connect,$status_query);
        $status_row=mysqli_fetch_assoc($status_stmt);
        if($status_row['order_status']==1){
            $query="UPDATE user_order SET user_status=8 WHERE order_number=".$order_number." AND USER_user_serial=".$user_serial;
            $stmt = mysqli_query($connect,$query);
            $confirm=1;
            $update_query="UPDATE Capstone.user SET user_mileage=user_mileage+(SELECT use_mileage FROM Capstone.user_order WHERE USER_user_serial=".$user_serial." AND order_number=".$order_number.") WHERE user_serial=".$user_serial;
            $update_stmt=mysqli_query($connect,$update_query);
        }else{
            $confirm=0;
        }

        $send_data=array(
            'confirm'=>$confirm,
            'order_status'=>$status_row['order_status']
        );
        echo json_encode($send_data);

    }