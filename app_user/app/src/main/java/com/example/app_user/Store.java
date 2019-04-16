package com.example.app_user;

public class Store {
    private int store_serial;
    private String store_name;
    private String store_phone;
    private String store_address;
    private float distance;

    public Store(String serial, String name, String branch_name, String address, String phone, String distance){
        this.store_address = address;
        this.store_name  = name;
        if(!branch_name.equals(""))
            store_name+=" "+branch_name;
        this.store_serial = Integer.parseInt(serial);
        this.distance = Float.parseFloat(distance);
        this.store_phone = phone;
    }

    public int getStore_serial() {
        return store_serial;
    }

    public void setStore_serial(int store_serial) {
        this.store_serial = store_serial;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_phone() {
        return store_phone;
    }

    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}