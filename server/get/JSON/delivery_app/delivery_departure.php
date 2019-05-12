<?php
    function delivery_departure($json_data){
        include '../db.php';
        date_default_timezone_set("Asia/Seoul");
        $current=date("Y-m-d H:i:s");

        $order_number=$json_data['order_number'];
        $departure_query="UPDATE store_order SET order_status=6, delivery_departure_time='".$current."' WHERE order_number=".$order_number;
        $departure_stmt = mysqli_query($connect,$departure_query);


        $query="
               SELECT DISTINCT tb2.*, user_name,user_phone
        	FROM
        		(
        		SELECT tb1.*,menu_name,menu_price,USER_user_serial, destination, destination_lat, destination_long
        		FROM
        			(
        			SELECT so.order_number, so.store_serial, order_receipt_date, s.store_name, s.store_latitude, s.store_longitude, s.store_address_jibun
        			FROM Capstone.store_order AS so
        			INNER JOIN Capstone.store AS s
        			ON so.store_serial=s.store_serial
        			WHERE so.order_number=".$order_number."
        			) tb1
        		INNER JOIN Capstone.order_menu AS o
        		INNER JOIN Capstone.menu AS m
                INNER JOIN Capstone.user_order AS uo
        		ON o.menu_serial=m.menu_serial AND tb1.order_number=o.order_number AND uo.user_order_serial=o.user_order_serial

        		) tb2

        	INNER JOIN Capstone.user AS u
			ON tb2.USER_user_serial=u.user_serial
        	ORDER BY order_receipt_date,tb2.order_number,USER_user_serial
        ";
        $stmt = mysqli_query($connect,$query);


          $order_num=0;
          $user_serial=0;
          $total=[];
          $total_price=0;
          $user_price=0;
          $menu=[];
          $user_order=[];

         while ($row = mysqli_fetch_assoc($stmt)) {

             if($order_num===0||$order_num==$row['order_number']){
                 $order_num=$row['order_number'];
             }else{

                 array_push($user_order,$user_menu);
                 $data['user_order']=$user_order;
                 $order_num=$row['order_number'];
                 $user_order=[];
                 $user_serial=$row['USER_user_serial'];
                 $menu=[];
             }
             if($user_serial===0||$user_serial==$row['USER_user_serial']){
                 $user_serial=$row['USER_user_serial'];

             }else{
                 array_push($user_order,$user_menu);
                 $user_serial=$row['USER_user_serial'];
                 $menu=[];
                 $user_price=0;
             }
             $user_price+=$row['menu_price'];
             $total_price+=$row['menu_price'];
             $menu_info=array(
                'menu_name'=>$row['menu_name'],
                'menu_price'=>$row['menu_price']
             );
             array_push($menu,$menu_info);
             $user_menu=array(
                                 'user_serial'=>$row['USER_user_serial'],
                                 'user_name'=>$row['user_name'],
                                 'user_phone'=>$row['user_phone'],
                                 "destination"=>$row['destination'],
                                 "destination_long"=>$row['destination_long'],
                                 "destination_lat"=>$row['destination_lat'],
                                 'menu'=>$menu,
                                 'user_total_price'=>$user_price
                 );
            $data=array(
                            "order_number"=>$order_num,
                            "store_serial"=>$row['store_serial'],
                            "store_name"=>$row['store_name'],
                            "store_address"=>$row['store_address_jibun'],
                            "store_latitude"=>$row['store_latitude'],
                            "store_longitude"=>$row['store_longitude'],
                            "user_order"=>$user_order,
                            "total_price"=>$total_price
                             );

         }
         array_push($user_order,$user_menu);
         $data['user_order']=$user_order;


        echo json_encode($data,JSON_UNESCAPED_UNICODE);
}