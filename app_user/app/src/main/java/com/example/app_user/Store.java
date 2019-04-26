package com.example.app_user;

import java.util.ArrayList;

public class Store {
    public ArrayList<Menu> menu_al=new ArrayList<>();
    private int store_serial;
    private String store_name;
    private String store_branch_name;
    private String store_address_jibun;
    private String store_building_name;
    private String start_time;
    private String end_time;
    private String store_restday;
    private String store_profile_img;
    private String store_main_type_name;
    private String store_sub_type_name;
    private String store_phone;
    private String store_address;
    private float distance;

    public Store(String serial, String name, String branch_name, String address, String phone, String distance){
        this.store_address = address;
        this.store_name  = name;
        this.store_branch_name = branch_name;
        this.store_serial = Integer.parseInt(serial);
        this.distance = Float.parseFloat(distance);
        this.store_phone = phone;
    }

    public String getStore_branch_name() {
        return store_branch_name;
    }

    public void setStore_branch_name(String store_branch_name) {
        this.store_branch_name = store_branch_name;
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