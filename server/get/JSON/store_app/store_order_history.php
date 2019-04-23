<?php
function store_order_history($json_data){
    include '../db.php';
        $store_serial=$json_data['store_serial'];
        $date=$json_data['date'];
        $query="
            SELECT DISTINCT tb2.*, user_name
        	FROM
        		(
        		SELECT tb1.*,menu_name,USER_user_serial, destination
        		FROM
        			(
        			SELECT so.order_number, so.store_serial, order_receipt_date
        			FROM Capstone.store_order AS so
        			INNER JOIN Capstone.store AS s
        			ON so.store_serial=s.store_serial
        			WHERE so.order_status=6 AND so.store_serial=".$store_serial."
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
          $menu=[];
          $user_order=[];

         while ($row = mysqli_fetch_assoc($stmt)) {

             if($order_num===0||$order_num==$row['order_number']){
                 $order_num=$row['order_number'];
             }else{
                 array_push($user_order,$user_menu);
                 $data=array(
                "order_number"=>$order_num,
                'order_receipt_date'=>$row['order_receipt_date'],
                "user_order"=>$user_order
                 );
                 array_push($total,$data);
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
             }

             array_push($menu,$row['menu_name']);
             $user_menu=array(
                                 'user_serial'=>$row['USER_user_serial'],
                                 'user_name'=>$row['user_name'],
                                 'destination'=>$row['destination'],
                                 'menu'=>$menu
                 );

             $data=array(
                "order_number"=>$order_num,
                'order_receipt_date'=>$row['order_receipt_date'],
                "user_order"=>$user_order
             );
         }
         array_push($user_order,$user_menu);
         $data['user_order']=$user_order;
         array_push($total,$data);


        echo json_encode($total,JSON_UNESCAPED_UNICODE);

}