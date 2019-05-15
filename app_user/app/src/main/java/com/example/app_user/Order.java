package com.example.app_user;

import java.util.ArrayList;

public class Order {

    private Store store;
    private ArrayList<User> user_al=new ArrayList<>();
    private String order_create_date;
    private int order_number;
    private int participate_person;
    private int total_order_price;



    public Order(String order_create_date,String participate_person, String total_order_price,String order_number){
        this.order_create_date=order_create_date;
        this.participate_person=Integer.parseInt(participate_person);
        this.total_order_price=Integer.parseInt(total_order_price);
        this.order_number=Integer.parseInt(order_number);
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

    public int getOrder_number() {
        return order_number;
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
}
