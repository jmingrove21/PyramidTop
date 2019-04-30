<?php
    function delivery_order($json_data){
         include '../db.php';
         $delivery_id=$json_data['delivery_id'];

         $query="SELECT destination, destination_lat, destination_long FROM user_order WHERE order_number=3";
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
                "destination_long"=>$row['destination_long'],
                "destination_lat"=>$row['destination_lat']
            );
            array_push($data,$d);
        }

        $send_data=array(
            'confirm'=>$confirm,
            'data'=>$data
        );
        echo json_encode($send_data,JSON_UNESCAPED_UNICODE);
}