package com.example.app_user.Item_dir;

import com.example.app_user.R;

import java.util.ArrayList;

public class Order {

    private Store store;
    private ArrayList<User> user_al=new ArrayList<>();
    private int participate_person;
    private int total_order_price;
    private int order_number;
    private int order_status;
    private String order_create_date;
    private String order_receipt_date;
    private String delivery_departure_time;
    private String delivery_arrival_time;
    private int my_order_total_price=0;
    private String my_pay_price;
    public Order(String order_create_date,String participate_person, String total_order_price, String order_number){
        this.order_create_date=order_create_date;
        this.participate_person=Integer.parseInt(participate_person);
        this.total_order_price=Integer.parseInt(total_order_price);
        this.order_number=Integer.parseInt(order_number);
    }
    public Order(String order_create_date,String participate_person, String total_order_price, String order_number,String order_status){
        this.order_create_date=order_create_date;
        this.participate_person=Integer.parseInt(participate_person);
        this.total_order_price=Integer.parseInt(total_order_price);
        this.order_number=Integer.parseInt(order_number);
        this.order_status=Integer.parseInt(order_status);
    }

    public void setStore(Store store) {
        this.store = store;
    }
    public Store getStore() {
        return this.store;
    }
    public ArrayList<User> getUser_al() {
        return user_al;
    }


    public int getParticipate_person() {
        return participate_person;
    }
    public String getOrder_create_date() {
        return order_create_date;
    }

    public int getTotal_order_price() {
        return total_order_price;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }
    public String getOrder_receipt_date() {
        return order_receipt_date;
    }

    public String getDelivery_departure_time() {
        return delivery_departure_time;
    }

    public String getDelivery_arrival_time() {
        return delivery_arrival_time;
    }
    public void setDelivery_arrival_time(String delivery_arrival_time) {
        this.delivery_arrival_time = delivery_arrival_time;
    }
    public int getMy_order_total_price() {
        return my_order_total_price;
    }

    public void setMy_order_total_price(int my_order_total_price) {
        this.my_order_total_price += my_order_total_price;
    }

    public String getMy_pay_price() {
        return my_pay_price;
    }

    public void setMy_pay_price(String my_pay_price) {
        this.my_pay_price = my_pay_price;
    }

    public void setDateSpecification(String order_create_date, String order_receipt_date, String delivery_departure_time){
        this.order_create_date=order_create_date;
        this.order_receipt_date=order_receipt_date;
        this.delivery_departure_time=delivery_departure_time;
    }
    public int getOrderStatus(){
        if(order_status==1)
            return R.drawable.wait;
        else if(order_status==3)
            return R.drawable.receipt;
        else if(order_status==4||order_status==5)
            //return "배달 준비";
            return R.drawable.delivery_ready;
        else if(order_status==6)
            return R.drawable.deliverying;
        else if(order_status==7)
            return R.drawable.delivery_ready;
        else
            return 0;
    }
}
