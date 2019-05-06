<?php
    function store_main($json_data){
         include '../db.php';
         $store_serial=$json_data['store_serial'];

       $query="
            SELECT tb1.*, m.menu_name
             FROM
             (
             SELECT s.order_number, s.order_status, s.order_receipt_date, s.delivery_departure_time, s.delivery_approve_time
             FROM Capstone.store_order AS s
             WHERE s.store_serial=".$store_serial."
             ) tb1
             INNER JOIN Capstone.order_menu AS o
             INNER JOIN Capstone.menu_info AS m
             ON tb1.order_number=o.order_number
             AND o.menu_serial=m.menu_serial
             WHERE tb1.order_number=o.order_number AND tb1.order_status BETWEEN 3 AND 5
             ORDER BY tb1.order_receipt_date DESC
             ";
         $stmt = mysqli_query($connect,$query);

         $menu=[];
         $order_num=0;
         $order_status=0;
         $total=[];

        while ($row = mysqli_fetch_assoc($stmt)) {

            if($order_num===0||$order_num==$row['order_number']){
                $order_receipt_date=$row['order_receipt_date'];
                $delivery_departure_time=$row['delivery_departure_time'];
                $delivery_approve_time=$row['delivery_approve_time'];

                $order_num=$row['order_number'];
                $order_status=$row['order_status'];
            }else{
                $data=array(
                'order_status'=>$order_status,
                'order_num'=>$order_num,
                'order_receipt_date'=>$order_receipt_date,
                'delivery_departure_date'=>$delivery_departure_time,
                'delivery_approve_time'=>$delivery_approve_time,
                'menu'=>$menu
                );
                $menu=[];
                array_push($total,$data);
                $order_receipt_date=$row['order_receipt_date'];
                $delivery_departure_time=$row['delivery_departure_time'];
                $delivery_approve_time=$row['delivery_approve_time'];
                $order_num=$row['order_number'];
                $order_status=$row['order_status'];
            }
            array_push($menu,$row['menu_name']);
            $data=array(
                'order_status'=>$order_status,
                'order_num'=>$order_num,
                'order_receipt_date'=>$order_receipt_date,
                'delivery_departure_date'=>$delivery_departure_time,
                'delivery_approve_time'=>$delivery_approve_time,
                'menu'=>$menu
                );
        }

        $data=array(
            'order_status'=>$order_status,
            'order_num'=>$order_num,
            'order_receipt_date'=>$order_receipt_date,
            'delivery_departure_date'=>$delivery_departure_time,
            'delivery_approve_time'=>$delivery_approve_time,
            'menu'=>$menu
         );
        array_push($total,$data);
  echo json_encode($total,JSON_UNESCAPED_UNICODE);

    }