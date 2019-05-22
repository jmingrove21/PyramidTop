package com.example.app_user;

import java.util.ArrayList;

public class Order {

    private Store store;
    private ArrayList<User> user_al=new ArrayList<>();
    private String order_create_date;
    private int participate_person;
    private int total_order_price;
    private int order_number=0;
    private int order_status;
    private String order_receipt_date;
    private String delivery_request_time;
    private String delivery_approve_time;
    private String delivery_departure_time;
    private String participate_persons;

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

    public void setDateSpecification(String order_receipt_date, String delivery_request_time, String delivery_approve_time, String delivery_departure_time){
        this.order_receipt_date=order_receipt_date;
        this.delivery_request_time=delivery_request_time;
        this.delivery_approve_time=delivery_departure_time;
        this.delivery_departure_time=delivery_departure_time;

    }
    public String getOrderStatus(){
        if(order_status==1)
            return "접수 대기";
        else if(order_status==3)
            return "접수 완료";
        else if(order_status==4||order_status==5)
            return "배달 준비";
        else if(order_status==6)
            return "배달 중";
        else if(order_status==7)
            return "배달 완료";
        else
            return "";
    }
}
