<?php
function history($json_data){
    include '../db.php';
        $store_serial=$json_data['store_serial'];
        $date=$json_data['date'];
        $query="SELECT store_order_serial,u.order_number,order_receipt_date,u.store_serial,destination,menu_name
                FROM
                (
                SELECT *
                FROM Capstone.store_order AS s
                WHERE s.order_status=6 AND left(s.order_receipt_date, 10)='".$date."' AND s.store_serial=".$store_serial."
                )tb1
                INNER JOIN Capstone.user_order AS u
                INNER JOIN Capstone.order_menu AS o
                INNER JOIN Capstone.menu AS m
                ON tb1.order_serial=u.order_serial
                AND u.order_serial=o.order_serial
                AND o.menu_serial=m.menu_serial
                ORDER BY tb1.order_receipt_date ASC
            ";
        $stmt = mysqli_query($connect,$query);

        $send_data=[];
        while($result = mysqli_fetch_row($stmt)){

        }
}