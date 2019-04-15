<?php
    function delivery_order($json_data){
         include '../db.php';
         $delivery_id=$json_data['delivery_id'];

         $query="SELECT destination, destination_x, destination_y FROM user_order WHERE order_number=1";
         $stmt = mysqli_query($connect,$query);
         $confirm=-1;
         if($stmt)
            $confirm=1;
         else
            $confirm=0;
         $data=[];

        while ($row = mysqli_fetch_assoc($stmt)) {
            $d=array(
                "destination"=>$row['destination'],
                "destination_x"=>$row['destination_x'],
                "destination_y"=>$row['destination_y']
            );
            array_push($data,$d);
        }

        $send_data=array(
            'confirm'=>$confirm,
            'data'=>$data
        );
        echo json_encode($send_data);
}