<?php
    function store_order($json_data){
         include '../db.php';
         $store_serial=$json_data['store_serial'];

       $query="
             SELECT tb1.order_receipt_date, m.menu_name
             FROM
             (
             SELECT s.order_serial,s.order_receipt_date
             FROM Capstone.store_order AS s
             WHERE s.store_serial=".$store_serial."
             ) tb1
             INNER JOIN Capstone.order_menu AS o
             INNER JOIN Capstone.menu AS m
             ON tb1.order_serial=o.order_serial
             AND o.menu_serial=m.menu_serial
             WHERE tb1.order_serial=o.order_serial
             ORDER BY tb1.order_receipt_date ASC
             ";
         $stmt = mysqli_query($connect,$query);

         $menu=[];
         $date=0;
         $total=[];

        while ($row = mysqli_fetch_row($stmt)) {

            if($date===0||strtotime($date)-strtotime($row[0])==0){
                $date=$row[0];
            }else{
                $data=array(
                'date'=>$date,
                'menu'=>$menu
                );
                $menu=[];
                array_push($total,$data);
                $date=$row[0];
            }
            array_push($menu,$row[1]);
        }
        $data=array(
                        'date'=>$date,
                        'menu'=>$menu
         );
        array_push($total,$data);
  echo json_encode($total,JSON_UNESCAPED_UNICODE);

    }