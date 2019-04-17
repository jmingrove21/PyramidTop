<?php
    function user_main($json_data){
         include '../db.php';
         $lat=$json_data['user_lat'];
         $long=$json_data['user_long'];
         $count=$json_data['user_count'];

         $query="SELECT store_serial, store_name, store_branch_name,store_phone, store_address_jibun, ( 6371 * acos( cos( radians(".$lat.") ) * cos( radians( store_latitude) )
                           * cos( radians( store_longitude ) - radians(".$long.") )
                           + sin( radians(".$lat.") ) * sin( radians( store_latitude ) ) ) ) AS distance FROM Capstone.store ORDER BY distance ASC LIMIT ".$count.",5";

         $total=[];
          $stmt = mysqli_query($connect,$query);
          while ($row = mysqli_fetch_row($stmt)) {
            $data = array(
            'store_serial'=>$row[0],
            'store_name'=>$row[1],
            'store_branch_name'=>$row[2],
            'store_phone'=>$row[3],
            'store_address'=>$row[4],
            'distance'=>$row[5]
            );
            array_push($total,$data);
          }

            echo json_encode($total,JSON_UNESCAPED_UNICODE);
          }