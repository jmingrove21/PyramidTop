<?php
    function store_order($json_data){
         include '../db.php';
         $store_serial=$json_data['store_serial'];

       $query="
            SELECT DISTINCT tb1.order_receipt_date, m.menu_name, tb1.order_number
             FROM
             (
             SELECT s.order_number, s.order_receipt_date
             FROM Capstone.store_order AS s
             WHERE s.store_serial=".$store_serial."
             ) tb1
             INNER JOIN Capstone.order_menu AS o
             INNER JOIN Capstone.menu_info AS m
             ON tb1.order_number=o.order_number
             AND o.menu_serial=m.menu_serial
             WHERE tb1.order_number=o.order_number
             ORDER BY tb1.order_number ASC
             ";
         $stmt = mysqli_query($connect,$query);

         $menu=[];
         $order_num=0;
         $total=[];

        while ($row = mysqli_fetch_row($stmt)) {

            if($order_num===0||$order_num==$row[2]){
                $date=$row[0];
                $order_num=$row[2];
            }else{
                $data=array(
                'order_num'=>$order_num,
                'date'=>$row[0],
                'menu'=>$menu
                );
                $menu=[];
                array_push($total,$data);
                $order_num=$row[2];
            }
            array_push($menu,$row[1]);
        }
        $data=array(
                        'order_num'=>$order_num,
                        'date'=>$date,
                        'menu'=>$menu
         );
        array_push($total,$data);
  echo json_encode($total,JSON_UNESCAPED_UNICODE);

    }