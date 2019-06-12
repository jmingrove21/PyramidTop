package com.example.app_delivery;

public class Delivery_list {

    private int store_serial;
    private String delivery_request_time;
    private String store_name;
    private String store_branch_name;
    private float distance;
    private int order_number;

    public Delivery_list(int order_number,int store_serial, String delivery_request_time, String store_name, String store_branch_name, float distance){
        this.store_name=store_name;
        this.order_number=order_number;
        this.delivery_request_time=delivery_request_time;
        this.store_branch_name=store_branch_name;
        if(!this.store_branch_name.equals(""))
            this.store_name=store_name.concat("-"+this.store_branch_name);
        this.distance=distance;
        this.store_serial=store_serial;

    }
    public Delivery_list(int order_number, String store_name, String store_branch_name){
        this.order_number=order_number;
        this.store_name=store_name;
        this.store_branch_name=store_branch_name;
        if(!this.store_branch_name.equals(""))
            this.store_name=store_name.concat("-"+this.store_branch_name);

    }
    public int getStore_serial() {
        return store_serial;
    }

    public String getDelivery_request_time() {
        return delivery_request_time;
    }

    public String getStore_name() {
        return store_name;
    }

    public float getDistance() {
        return distance;
    }

    public int getOrder_number() {
        return order_number;
    }
}
