<?php
    function alarm_check($json_data){
        include "../db.php";
        $user_serial=$json_data['user_serial'];

        $query="
        SELECT store_name,order_status,user_status,so.order_number,order_receipt_date,delivery_request_time,delivery_departure_time,arrival_time
        FROM
        (
        	SELECT order_number,user_status,arrival_time
        	FROM Capstone.user_order
        	WHERE USER_user_serial=".$user_serial."
        ) tb
        INNER JOIN Capstone.store_order AS so
        INNER JOIN Capstone.store AS s
        ON so.order_number=tb.order_number
        AND s.store_serial=so.store_serial
        ORDER BY so.order_number ASC";
        $stmt = mysqli_query($connect,$query);

        $confirm=0;
        $check=1;
        $order_number=0;
        $order_status=0;
        $data=[];
        $user_alarm=[];

        while($row=mysqli_fetch_assoc($stmt)){
            if($row['order_status']==1||$row['order_status']==2||$row['order_status']==7||$row['order_status']==8){
            }else{
                    $order_number=$row['order_number'];
                    $order_status=$row['order_status'];
                    if($row['order_status']==3){
                        if($row['user_status']==0){
                            $info=array(
                                'status'=>3,
                                'time'=>$row['order_receipt_date']
                            );
                            array_push($data,$info);
                            $total=array(
                                'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                'alarm_check'=>$data
                            );
                        }else{
                            $check=0;
                        }
                    }else if($row['order_status']==4 || $row['order_status']==5){
                        if($row['user_status']!=4){
                            if($row['user_status']==0){
                            print_r("ee");
                                $info=array(
                                    'status'=>3,
                                    'time'=>$row['order_receipt_date']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>4,
                                    'time'=>$row['delivery_request_time']
                                );
                                array_push($data,$info);
                            $total=array(
                                'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                'alarm_check'=>$data
                            );
                            }else if($row['user_status']==3){
                                $info=array(
                                    'status'=>4,
                                    'time'=>$row['delivery_request_time']
                                );
                                array_push($data,$info);
                                 $total=array(
                                     'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                     'alarm_check'=>$data
                                 );
                            }
                        }else{
                            $check=0;
                        }

                    }else if($row['order_status']==6){
                        if($row['user_status']!=6){
                            if($row['user_status']==0){
                                $info=array(
                                    'status'=>3,
                                    'time'=>$row['order_receipt_date']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>4,
                                    'time'=>$row['delivery_request_time']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>6,
                                    'time'=>$row['delivery_departure_time']
                                );
                                array_push($data,$info);
                                $total=array(
                                    'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                    'alarm_check'=>$data
                                );

                            }else if($row['user_status']==3){
                                $info=array(
                                    'status'=>4,
                                    'time'=>$row['delivery_request_time']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>6,
                                    'time'=>$row['delivery_departure_time']
                                );
                                array_push($data,$info);
                                $total=array(
                                    'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                    'alarm_check'=>$data
                                );
                            }else if($row['user_status']==4)
                                $info=array(
                                    'status'=>6,
                                    'time'=>$row['delivery_departure_time']
                                );
                                array_push($data,$info);
                                $total=array(
                                    'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                    'alarm_check'=>$data
                                );
                        }else{
                            $check=0;
                        }
                    }else if(!isset($row['arrival_time'])){
                        if($row['user_status']==0){
                                $info=array(
                                    'status'=>3,
                                    'time'=>$row['order_receipt_date']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>4,
                                    'time'=>$row['delivery_request_time']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>6,
                                    'time'=>$row['delivery_departure_time']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>7,
                                    'time'=>$row['arrival_time']
                                );
                                array_push($data,$info);
                                $total=array(
                                    'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                    'alarm_check'=>$data
                                );
                            }else if($row['user_status']==3){
                                $info=array(
                                    'status'=>4,
                                    'time'=>$row['delivery_request_time']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>6,
                                    'time'=>$row['delivery_departure_time']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>7,
                                    'time'=>$row['arrival_time']
                                );
                                array_push($data,$info);
                                $total=array(
                                    'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                    'alarm_check'=>$data
                                );
                            }else if($row['user_status']==4){
                                $info=array(
                                    'status'=>6,
                                    'time'=>$row['delivery_departure_time']
                                );
                                array_push($data,$info);
                                $info=array(
                                    'status'=>7,
                                    'time'=>$row['arrival_time']
                                );
                                array_push($data,$info);
                                $total=array(
                                    'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                    'alarm_check'=>$data
                                );
                             }else if($row['user_status']==6){
                                $info=array(
                                    'status'=>7,
                                    'time'=>$row['arrival_time']
                                );
                                array_push($data,$info);
                                $total=array(
                                    'store_name'=>$row['store_name'],
                                'order_number'=>$order_number,
                                    'alarm_check'=>$data
                                );
                             }else{
                                $check=0;
                             }
                        }else{
                            $check=0;
                        }


                        if($check==1){
                            array_push($user_alarm,$total);
                            $query1="UPDATE user_order SET user_status=".$order_status." WHERE order_number=".$order_number." AND USER_user_serial=".$user_serial;
                            $stmt1 = mysqli_query($connect,$query1);
                            $confirm=1;
                        }else if($check==0){
                            $check=1;
                        }
                            $data=[];

                }
        }
        if($check==1){
        }else{
            array_push($user_alarm,$total);
            $query1="UPDATE user_order SET user_status=".$order_status." WHERE order_number=".$order_number." AND USER_user_serial=".$user_serial;
            $stmt1 = mysqli_query($connect,$query1);
            $confirm=1;
        }

        $result=array(
            'confirm'=>$confirm,
            'data'=>$user_alarm
        );
        echo json_encode($result,JSON_UNESCAPED_UNICODE);
    }